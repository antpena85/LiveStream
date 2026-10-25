package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PendingProbeTest {
    @Test
    fun terminalCancellationReleasesPendingStateAndRejectsLateAcknowledgment() {
        val pending = PendingProbe("watch", "request-10")
        pending.complete()
        assertThat(pending.isPending()).isFalse()
        assertThat(pending.acknowledge("watch", "request-10")).isFalse()
        pending.complete()
        assertThat(pending.isPending()).isFalse()
    }

    @Test
    fun keepAwakeEligibilityLastsOnlyUntilMatchingAcknowledgment() {
        val pending = PendingProbe("watch", "request-9")
        assertThat(pending.isPending()).isTrue()
        pending.acknowledge("other", "request-9")
        assertThat(pending.isPending()).isTrue()
        pending.acknowledge("watch", "request-9")
        assertThat(pending.isPending()).isFalse()
    }

    @Test
    fun onlyExpectedPeerAndTokenCompletePendingRequestOnce() {
        val pending = PendingProbe("watch", "request-7")

        assertThat(pending.acknowledge("other", "request-7")).isFalse()
        assertThat(pending.acknowledge("watch", "request-8")).isFalse()
        assertThat(pending.acknowledge("watch", "request-7")).isTrue()
        assertThat(pending.acknowledge("watch", "request-7")).isFalse()
    }
}