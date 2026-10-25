package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PendingProbeTest {
    @Test
    fun onlyExpectedPeerAndTokenCompletePendingRequestOnce() {
        val pending = PendingProbe("watch", "request-7")

        assertThat(pending.acknowledge("other", "request-7")).isFalse()
        assertThat(pending.acknowledge("watch", "request-8")).isFalse()
        assertThat(pending.acknowledge("watch", "request-7")).isTrue()
        assertThat(pending.acknowledge("watch", "request-7")).isFalse()
    }
}