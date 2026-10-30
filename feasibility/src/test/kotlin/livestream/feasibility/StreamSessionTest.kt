package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StreamSessionTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    @Test
    fun missingFitAssessmentAfterStopConfirmationCannotStart() {
        val database = temporaryFolder.root.toPath().resolve("unknown-fit.sqlite")
        DurableCommandProbe(database).use { store ->
            store.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a")
            assertThat(store.confirmStopped("session-a")).isTrue()
        }
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(session.requestStart(confirmed = true, cameraPermission = true))
                .isEqualTo(StartResult.NumericFitUnresolved)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
            assertThat(store.snapshot()).isEqualTo(ProbeSnapshot(8, 1, 0))
        }
    }

    @Test
    fun stopConfirmationDoesNotClearUnresolvedNumericFitForStart() {
        val database = temporaryFolder.root.toPath().resolve("unresolved-fit.sqlite")
        DurableCommandProbe(database).use { store ->
            store.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a")
            assertThat(store.confirmStopped("session-a")).isTrue()
        }
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(store.pendingStopSessions()).isEmpty()
            assertThat(session.requestStart(confirmed = true, cameraPermission = true, numericFitReady = false))
                .isEqualTo(StartResult.NumericFitUnresolved)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
            assertThat(store.snapshot()).isEqualTo(ProbeSnapshot(8, 1, 0))
        }
    }

    @Test
    fun confirmedStartWithDurablePendingStopAfterReopenIsDenied() {
        val database = temporaryFolder.root.toPath().resolve("pending-start.sqlite")
        DurableCommandProbe(database).use { store ->
            store.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a")
        }
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(session.requestStart(confirmed = true, cameraPermission = true))
                .isEqualTo(StartResult.PendingStop)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
            assertThat(store.pendingStopSessions()).containsExactly("session-a")
            assertThat(store.snapshot()).isEqualTo(ProbeSnapshot(8, 1, 0))
        }
    }

    @Test
    fun confirmedStartWithoutMicrophonePermissionIsDeniedAndRemainsStopped() {
        val session = StreamSession()

        val result = session.requestStart(
            confirmed = true,
            cameraPermission = true,
            microphonePermission = false,
        )

        assertThat(result).isEqualTo(StartResult.MicrophonePermissionRequired)
        assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
    }

    @Test
    fun confirmedStartWithoutCameraPermissionIsDeniedAndRemainsStopped() {
        val session = StreamSession()

        val result = session.requestStart(confirmed = true, cameraPermission = false)

        assertThat(result).isEqualTo(StartResult.CameraPermissionRequired)
        assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
    }

    @Test
    fun unconfirmedStartDoesNotBeginConnectingEvenWithCameraPermission() {
        val session = StreamSession()

        val result = session.requestStart(confirmed = false, cameraPermission = true)

        assertThat(result).isEqualTo(StartResult.Cancelled)
        assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
    }
}
