package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import java.io.ByteArrayInputStream
import java.io.EOFException
import java.io.FilterInputStream
import java.net.ProtocolException
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DiagnosticPingDecoderTest {
    @Test
    fun consecutivePingsPreserveTheirDistinctIdsInOrder() {
        val input = ByteArrayInputStream(byteArrayOf(
            0, 0, 0, 18, 1, 1,
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1,
            0, 0, 0, 18, 1, 1,
            -1, -26, -43, -60, -77, -94, -111, -128,
            119, 102, 85, 68, 51, 34, 17, 0
        ))

        val first = checkNotNull(DiagnosticPingDecoder.read(input))
        val second = checkNotNull(DiagnosticPingDecoder.read(input))

        assertThat(first.requestId).isEqualTo(listOf<Byte>(
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1
        ))
        assertThat(second.requestId).isEqualTo(listOf<Byte>(
            -1, -26, -43, -60, -77, -94, -111, -128,
            119, 102, 85, 68, 51, 34, 17, 0
        ))
        assertThat(DiagnosticPingDecoder.read(input)).isNull()
    }

    @Test
    fun partialNextHeaderAfterPingIsTruncationNotCleanEof() {
        val input = ByteArrayInputStream(byteArrayOf(
            0, 0, 0, 18, 1, 1,
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1,
            0
        ))

        val ping = checkNotNull(DiagnosticPingDecoder.read(input))
        assertThat(ping.requestId).isEqualTo(listOf<Byte>(
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1
        ))
        assertThrows(EOFException::class.java) {
            DiagnosticPingDecoder.read(input)
        }
    }

    @Test
    fun cleanEofAfterCompletePingReturnsNullOnNextRead() {
        val input = ByteArrayInputStream(byteArrayOf(
            0, 0, 0, 18, 1, 1,
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1
        ))

        val ping = checkNotNull(DiagnosticPingDecoder.read(input))
        assertThat(ping.requestId).isEqualTo(listOf<Byte>(
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1
        ))
        assertThat(DiagnosticPingDecoder.read(input)).isNull()
    }

    @Test
    fun cleanEofBeforeHeaderReturnsNull() {
        assertThat(DiagnosticPingDecoder.read(ByteArrayInputStream(byteArrayOf()))).isNull()
    }

    @Test
    fun truncatedLengthHeaderCannotProduceAPing() {
        // EOF after three of the four bytes of the length header.
        val input = ByteArrayInputStream(byteArrayOf(0, 0, 0))

        assertThrows(EOFException::class.java) {
            DiagnosticPingDecoder.read(input)
        }
    }

    @Test
    fun truncatedRequestIdCannotProduceAPing() {
        // The advertised 18-byte body ends after only 15 of 16 ID bytes.
        val input = ByteArrayInputStream(byteArrayOf(
            0, 0, 0, 18, 1, 1,
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26
        ))

        assertThrows(EOFException::class.java) {
            DiagnosticPingDecoder.read(input)
        }
    }

    @Test
    fun validPingPreservesIdWhenStreamReturnsShortReads() {
        val bytes = byteArrayOf(
            0, 0, 0, 18, 1, 1,
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1,
            42
        )
        val input = object : FilterInputStream(ByteArrayInputStream(bytes)) {
            override fun read(buffer: ByteArray, offset: Int, length: Int): Int =
                super.read(buffer, offset, minOf(length, 1))
        }

        val ping = checkNotNull(DiagnosticPingDecoder.read(input))

        assertThat(ping.requestId).isEqualTo(listOf<Byte>(
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1
        ))
        assertThat(input.read()).isEqualTo(42)
    }

    @Test
    fun oversizedV1BodyWithinEnvelopeIsRejectedInsteadOfReturningAPing() {
        // A complete 19-byte body has an extra byte beyond the fixed v1 ping.
        val input = ByteArrayInputStream(byteArrayOf(
            0, 0, 0, 19, 1, 1,
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1, 42
        ))

        assertThrows(ProtocolException::class.java) {
            DiagnosticPingDecoder.read(input)
        }
    }

    @Test
    fun undersizedAdvertisedBodyIsRejectedInsteadOfReturningAPing() {
        // A complete 17-byte body is one byte short of the v1 ping length.
        val input = ByteArrayInputStream(byteArrayOf(
            0, 0, 0, 17, 1, 1,
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26
        ))

        assertThrows(ProtocolException::class.java) {
            DiagnosticPingDecoder.read(input)
        }
    }

    @Test
    fun unknownMessageTypeIsRejectedInsteadOfReturningAPing() {
        // An otherwise valid v1 body uses unknown type 3.
        val input = ByteArrayInputStream(byteArrayOf(
            0, 0, 0, 18, 1, 3,
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1
        ))

        assertThrows(ProtocolException::class.java) {
            DiagnosticPingDecoder.read(input)
        }
    }

    @Test
    fun acknowledgmentFrameIsRejectedInsteadOfReturningAPing() {
        // Valid v1 envelope and ID, but type 2 is an acknowledgment, not a ping.
        val input = ByteArrayInputStream(byteArrayOf(
            0, 0, 0, 18, 1, 2,
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1
        ))

        assertThrows(ProtocolException::class.java) {
            DiagnosticPingDecoder.read(input)
        }
    }

    @Test
    fun unsupportedVersionIsRejectedInsteadOfReturningAPing() {
        // Otherwise valid fixed-length ping, but protocol version 2 is unsupported.
        val input = ByteArrayInputStream(byteArrayOf(
            0, 0, 0, 18, 2, 1,
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1
        ))

        assertThrows(ProtocolException::class.java) {
            DiagnosticPingDecoder.read(input)
        }
    }

    @Test
    fun validV1PingPreservesEveryRequestIdByteAndLeavesNextFrameUnread() {
        // Length 18, version 1, type 1, sixteen opaque ID bytes, next-frame sentinel.
        val input = ByteArrayInputStream(byteArrayOf(
            0, 0, 0, 18, 1, 1,
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1,
            42
        ))

        val ping = checkNotNull(DiagnosticPingDecoder.read(input))

        assertThat(ping.requestId).isEqualTo(listOf<Byte>(
            0, 17, 34, 51, 68, 85, 102, 119,
            -128, -111, -94, -77, -60, -43, -26, -1
        ))
        assertThat(input.read()).isEqualTo(42)
        assertThat(input.read()).isEqualTo(-1)
    }
}