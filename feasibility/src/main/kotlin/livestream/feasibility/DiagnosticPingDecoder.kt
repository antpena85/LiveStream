package livestream.feasibility

import java.io.DataInputStream
import java.io.InputStream
import java.io.PushbackInputStream
import java.net.ProtocolException

/** Host-only v1 ping decoding; no transport, authentication or deadline handling. */
object DiagnosticPingDecoder {
    data class Ping(val requestId: List<Byte>)

    /** Returns null only when the stream ends before the next frame begins. */
    fun read(input: InputStream): Ping? {
        val frame = PushbackInputStream(input, 1)
        val first = frame.read()
        if (first == -1) return null
        frame.unread(first)
        val length = DiagnosticFrameHeader.readBodyLength(frame)
        if (length != 18L) throw ProtocolException("V1 ping body must contain eighteen bytes")
        val body = DataInputStream(frame)
        if (body.readUnsignedByte() != 1) throw ProtocolException("Unsupported diagnostic version")
        if (body.readUnsignedByte() != 1) throw ProtocolException("Expected diagnostic ping")
        val requestId = ByteArray(16)
        body.readFully(requestId)
        return Ping(requestId.toList())
    }
}