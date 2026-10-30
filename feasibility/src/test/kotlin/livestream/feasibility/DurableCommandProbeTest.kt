package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertThrows
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Path
import java.util.concurrent.Callable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class DurableCommandProbeTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    @Test
    fun distinctUnresolvedStopsSurviveScoringAndReopenUntilIndividuallyConfirmed() {
        val database = temporaryFolder.root.toPath().resolve("multiple-stops.sqlite")
        DurableCommandProbe(database).use { probe ->
            assertThat(probe.recordPoint("a", 0, "session-a"))
                .isEqualTo(ProbeSnapshot(8, 1, 0))
            assertThat(probe.recordPoint("b", 0, "session-b"))
                .isEqualTo(ProbeSnapshot(9, 2, 0))
            assertThat(probe.pendingStopSessions()).containsExactly("session-a", "session-b")
        }
        DurableCommandProbe(database).use { probe ->
            assertThat(probe.pendingStopSessions()).containsExactly("session-a", "session-b")
            assertThat(probe.recordPoint("b", 0, "session-b")).isEqualTo(ProbeSnapshot(9, 2, 0))
            assertThat(probe.confirmStopped("session-a")).isTrue()
            assertThat(probe.pendingStopSessions()).containsExactly("session-b")
            assertThat(probe.snapshot()).isEqualTo(ProbeSnapshot(9, 2, 0))
        }
        DurableCommandProbe(database).use { probe ->
            assertThat(probe.pendingStopSessions()).containsExactly("session-b")
            assertThat(probe.confirmStopped("session-b")).isTrue()
            assertThat(probe.pendingStopSessions()).isEmpty()
        }
    }

    @Test
    fun delayedStopConfirmationCannotClearNewerSessionsPendingStop() {
        val database = temporaryFolder.root.toPath().resolve("stale-stop.sqlite")
        DurableCommandProbe(database).use { probe ->
            probe.recordPoint("first", expectedEpoch = 0, fitFailureSession = "session-a")
            assertThat(probe.confirmStopped("session-a")).isTrue()
            assertThat(probe.pendingStopSession()).isNull()
            probe.recordPoint("second", expectedEpoch = 0, fitFailureSession = "session-b")
        }
        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.confirmStopped("session-a")).isFalse()
            assertThat(reopened.pendingStopSession()).isEqualTo("session-b")
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
        }
        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.pendingStopSession()).isEqualTo("session-b")
            assertThat(reopened.confirmStopped("session-b")).isTrue()
            assertThat(reopened.confirmStopped("session-b")).isFalse()
            assertThat(reopened.pendingStopSession()).isNull()
        }
    }

    @Test
    fun committedFitFailureWithLostAcknowledgmentRetainsReceiptAndStopMarker() {
        val database = temporaryFolder.root.toPath().resolve("lost-fit-ack.sqlite")
        DurableCommandProbe(database).use { probe ->
            assertThat(probe.recordPoint("previous", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
        }
        assertThat(interruptProcess(database, "fit-point", "interrupt-fit-after-commit").trim())
            .isEqualTo("fit-after-commit")

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
            assertThat(reopened.pendingStopSession()).isEqualTo("session-a")
            assertThat(reopened.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a"))
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
            assertThat(reopened.recordPoint("later", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 10, revision = 3, epoch = 0))
            assertThat(reopened.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a"))
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 10, revision = 3, epoch = 0))
            assertThat(reopened.pendingStopSession()).isEqualTo("session-a")
        }
        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a"))
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 10, revision = 3, epoch = 0))
            assertThat(reopened.pendingStopSession()).isEqualTo("session-a")
        }
    }

    @Test
    fun interruptedFitFailureBeforeCommitPreservesPriorStateAndAllowsOneRetry() {
        val database = temporaryFolder.root.toPath().resolve("interrupted-fit.sqlite")
        DurableCommandProbe(database).use { probe ->
            assertThat(probe.recordPoint("previous", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
            assertThat(probe.pendingStopSession()).isNull()
        }

        val output = interruptProcess(database, "fit-point", "interrupt-fit-before-commit")
        assertThat(output.trim()).isEqualTo("fit-before-commit")

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
            assertThat(reopened.pendingStopSession()).isNull()
            assertThat(reopened.recordPoint("previous", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
            assertThat(reopened.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a"))
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
            assertThat(reopened.pendingStopSession()).isEqualTo("session-a")
            assertThat(reopened.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a"))
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
        }
        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
            assertThat(reopened.pendingStopSession()).isEqualTo("session-a")
        }
    }

    @Test
    fun fitFailureRetainsAcceptedPointReceiptAndPendingStopAfterReopen() {
        val database = temporaryFolder.root.toPath().resolve("pending-stop.sqlite")
        DurableCommandProbe(database).use { probe ->
            assertThat(probe.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a"))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
        }
        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
            assertThat(reopened.pendingStopSession()).isEqualTo("session-a")
            assertThat(reopened.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a"))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
        }
    }

    @Test
    fun confirmedResetStartsDurableNewEpochWithZeroPointsAndRevision() {
        val database = temporaryFolder.root.toPath().resolve("reset-match.sqlite")
        DurableCommandProbe(database).use { probe ->
            assertThat(probe.recordPoint("before-reset", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
            assertThat(probe.reset(confirmed = true, expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 0, revision = 0, epoch = 1))
        }

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot())
                .isEqualTo(ProbeSnapshot(points = 0, revision = 0, epoch = 1))
            assertThat(reopened.recordPoint("after-reset", expectedEpoch = 1))
                .isEqualTo(ProbeSnapshot(points = 1, revision = 1, epoch = 1))
        }
    }

    @Test
    fun oldPointCommandsCannotChangeResetMatchAfterReopenEvenWhenRevisionsCoincide() {
        val database = temporaryFolder.root.toPath().resolve("late-match.sqlite")
        DurableCommandProbe(database).use { probe ->
            assertThat(probe.recordPoint("accepted-before-reset", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
            probe.reset(confirmed = true, expectedEpoch = 0)
            assertThat(probe.recordPoint("new-match-point", expectedEpoch = 1))
                .isEqualTo(ProbeSnapshot(points = 1, revision = 1, epoch = 1))
        }

        DurableCommandProbe(database).use { reopened ->
            for (commandId in listOf("accepted-before-reset", "never-delivered-old-point")) {
                val rejected = assertThrows(StaleProbeMatch::class.java) {
                    reopened.recordPoint(commandId, expectedEpoch = 0)
                }
                assertThat(rejected.current)
                    .isEqualTo(ProbeSnapshot(points = 1, revision = 1, epoch = 1))
                assertThat(reopened.snapshot())
                    .isEqualTo(ProbeSnapshot(points = 1, revision = 1, epoch = 1))
            }
            assertThat(reopened.recordPoint("another-current-point", expectedEpoch = 1))
                .isEqualTo(ProbeSnapshot(points = 2, revision = 2, epoch = 1))
        }
    }

    @Test
    fun commandIdInNewEpochHasItsOwnDurableReceiptAndAppliesOnlyOnce() {
        val database = temporaryFolder.root.toPath().resolve("epoch-receipts.sqlite")
        DurableCommandProbe(database).use { probe ->
            assertThat(probe.recordPoint("reused-point-id", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
            probe.reset(confirmed = true, expectedEpoch = 0)
        }

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.recordPoint("reused-point-id", expectedEpoch = 1))
                .isEqualTo(ProbeSnapshot(points = 1, revision = 1, epoch = 1))
            assertThat(reopened.recordPoint("reused-point-id", expectedEpoch = 1))
                .isEqualTo(ProbeSnapshot(points = 1, revision = 1, epoch = 1))
            assertThat(reopened.recordPoint("next-point", expectedEpoch = 1))
                .isEqualTo(ProbeSnapshot(points = 2, revision = 2, epoch = 1))
        }

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.recordPoint("reused-point-id", expectedEpoch = 1))
                .isEqualTo(ProbeSnapshot(points = 1, revision = 1, epoch = 1))
            assertThat(reopened.snapshot())
                .isEqualTo(ProbeSnapshot(points = 2, revision = 2, epoch = 1))
        }
    }

    @Test
    fun canceledResetPreservesEpochPointsRevisionAndReceiptsAfterReopen() {
        val database = temporaryFolder.root.toPath().resolve("canceled-reset.sqlite")
        DurableCommandProbe(database).use { probe ->
            probe.recordPoint("kept-point", expectedEpoch = 0)
            assertThat(probe.reset(confirmed = false, expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
            assertThat(probe.recordPoint("next-point", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
        }

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.recordPoint("kept-point", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
            assertThat(reopened.snapshot())
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
        }
    }

    @Test
    fun processInterruptedDuringResetPreservesOldEpochAndAllowsConfirmedRetry() {
        val database = temporaryFolder.root.toPath().resolve("interrupted-reset.sqlite")
        DurableCommandProbe(database).use { it.recordPoint("accepted-before-reset", expectedEpoch = 0) }

        interruptProcess(database, "reset", "interrupt-reset-before-commit")

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot())
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
            assertThat(reopened.recordPoint("accepted-before-reset", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
            assertThat(reopened.reset(confirmed = true, expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 0, revision = 0, epoch = 1))
            assertThat(reopened.recordPoint("first-new-point", expectedEpoch = 1))
                .isEqualTo(ProbeSnapshot(points = 1, revision = 1, epoch = 1))
        }

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot())
                .isEqualTo(ProbeSnapshot(points = 1, revision = 1, epoch = 1))
        }
    }

    @Test
    fun acceptedPointSurvivesReopenAndRetryDoesNotApplyItAgain() {
        val database = temporaryFolder.root.toPath().resolve("match.sqlite")
        val accepted = DurableCommandProbe(database).use { it.recordPoint("point-1", expectedEpoch = 0) }
        assertThat(accepted).isEqualTo(ProbeSnapshot(points = 8, revision = 1))

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 8, revision = 1))
            assertThat(reopened.recordPoint("point-1", expectedEpoch = 0)).isEqualTo(accepted)
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 8, revision = 1))
        }
    }

    @Test
    fun committedPointWithLostAcknowledgmentRetainsOriginalReceiptWithoutReapplyingAfterReopen() {
        val database = temporaryFolder.root.toPath().resolve("lost-acknowledgment.sqlite")
        DurableCommandProbe(database).use { probe ->
            assertThat(probe.recordPoint("accepted-before-interruption", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1, epoch = 0))
        }

        val response = interruptProcess(database, "unacknowledged-point", "interrupt-after-commit")
        assertThat(response).isEmpty()

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot())
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
            assertThat(reopened.recordPoint("unacknowledged-point", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
            assertThat(reopened.snapshot())
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
            assertThat(reopened.recordPoint("later-point", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 10, revision = 3, epoch = 0))
            assertThat(reopened.recordPoint("unacknowledged-point", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
            assertThat(reopened.snapshot())
                .isEqualTo(ProbeSnapshot(points = 10, revision = 3, epoch = 0))
        }

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.recordPoint("unacknowledged-point", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2, epoch = 0))
            assertThat(reopened.snapshot())
                .isEqualTo(ProbeSnapshot(points = 10, revision = 3, epoch = 0))
        }
    }

    @Test
    fun concurrentPointCommandsReceiveDistinctDurableRevisions() {
        val database = temporaryFolder.root.toPath().resolve("concurrent-match.sqlite")
        DurableCommandProbe(database).use { probe ->
            val workers = Executors.newFixedThreadPool(8)
            val ready = CountDownLatch(8)
            val start = CountDownLatch(1)
            try {
                val pending = (1..8).map { command ->
                    workers.submit(Callable {
                        ready.countDown()
                        check(start.await(10, TimeUnit.SECONDS)) { "Concurrent start timed out" }
                        probe.recordPoint("concurrent-point-$command", expectedEpoch = 0)
                    })
                }
                assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue()
                start.countDown()
                val receipts = pending.map { it.get(10, TimeUnit.SECONDS) }

                assertThat(receipts).containsExactly(
                    ProbeSnapshot(points = 8, revision = 1),
                    ProbeSnapshot(points = 9, revision = 2),
                    ProbeSnapshot(points = 10, revision = 3),
                    ProbeSnapshot(points = 11, revision = 4),
                    ProbeSnapshot(points = 12, revision = 5),
                    ProbeSnapshot(points = 13, revision = 6),
                    ProbeSnapshot(points = 14, revision = 7),
                    ProbeSnapshot(points = 15, revision = 8),
                )
                assertThat(probe.snapshot()).isEqualTo(ProbeSnapshot(points = 15, revision = 8))
            } finally {
                start.countDown()
                workers.shutdownNow()
                check(workers.awaitTermination(5, TimeUnit.SECONDS)) { "Command workers did not terminate" }
            }
        }

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 15, revision = 8))
        }
    }

    @Test
    fun simultaneousRetriesOfOnePointApplyOnceAndSurviveReopen() {
        val database = temporaryFolder.root.toPath().resolve("duplicate-match.sqlite")
        DurableCommandProbe(database).use { probe ->
            val workers = Executors.newFixedThreadPool(8)
            val ready = CountDownLatch(8)
            val start = CountDownLatch(1)
            try {
                val pending = (1..8).map {
                    workers.submit(Callable {
                        ready.countDown()
                        check(start.await(10, TimeUnit.SECONDS)) { "Concurrent start timed out" }
                        probe.recordPoint("shared-point", expectedEpoch = 0)
                    })
                }
                assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue()
                start.countDown()
                pending.forEach { receipt ->
                    assertThat(receipt.get(10, TimeUnit.SECONDS))
                        .isEqualTo(ProbeSnapshot(points = 8, revision = 1))
                }
                assertThat(probe.snapshot()).isEqualTo(ProbeSnapshot(points = 8, revision = 1))
            } finally {
                start.countDown()
                workers.shutdownNow()
                check(workers.awaitTermination(5, TimeUnit.SECONDS)) { "Command workers did not terminate" }
            }
        }

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 8, revision = 1))
            assertThat(reopened.recordPoint("shared-point", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1))
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 8, revision = 1))
        }
    }

    @Test
    fun processInterruptedBeforeCommitPreservesAcceptedStateAndAllowsOneRetry() {
        val database = temporaryFolder.root.toPath().resolve("interrupted-match.sqlite")
        DurableCommandProbe(database).use {
            assertThat(it.recordPoint("accepted-before-interruption", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 8, revision = 1))
        }

        interruptProcess(database, "interrupted-point")

        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 8, revision = 1))
            assertThat(reopened.recordPoint("interrupted-point", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2))
            assertThat(reopened.recordPoint("interrupted-point", expectedEpoch = 0))
                .isEqualTo(ProbeSnapshot(points = 9, revision = 2))
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(points = 9, revision = 2))
        }
    }

    private fun interruptProcess(
        database: Path,
        commandId: String,
        operation: String = "interrupt-before-commit",
    ): String {
        val output = temporaryFolder.newFile("child-output.txt")
        val java = Path.of(System.getProperty("java.home"), "bin", "java.exe")
        val child = ProcessBuilder(
            java.toString(),
            "-cp", System.getProperty("feasibility.runtimeClasspath"),
            "livestream.feasibility.InterruptedWriteProcessKt",
            database.toString(), commandId, operation,
        ).redirectErrorStream(true).redirectOutput(output).start()
        try {
            assertThat(child.waitFor(20, TimeUnit.SECONDS)).isTrue()
            com.google.common.truth.Truth.assertWithMessage(output.readText())
                .that(child.exitValue()).isEqualTo(23)
            return output.readText()
        } finally {
            if (child.isAlive) {
                child.destroyForcibly()
                check(child.waitFor(5, TimeUnit.SECONDS)) { "Child JVM did not terminate" }
            }
        }
    }
}
