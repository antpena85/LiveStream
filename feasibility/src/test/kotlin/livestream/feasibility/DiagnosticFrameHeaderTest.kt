package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import java.io.ByteArrayInputStream
import java.net.ProtocolException
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DiagnosticFrameHeaderTest {
    @Test
    fun oversizedBodyLengthIsRejectedWithoutConsumingBodyBytes() {
        // Unsigned big-endian length 257, followed by two sentinel body bytes.
        val input = ByteArrayInputStream(byteArrayOf(0, 0, 1, 1, 42, 43))

        assertThrows(ProtocolException::class.java) {
            DiagnosticFrameHeader.readBodyLength(input)
        }

        assertThat(input.read()).isEqualTo(42)
        assertThat(input.read()).isEqualTo(43)
        assertThat(input.read()).isEqualTo(-1)
    }
}