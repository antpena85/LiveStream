package livestream.feasibility

import java.io.DataInputStream
import java.io.InputStream
import java.net.ProtocolException

/** Host-only header inspection, not a full frame decoder or network adapter. */
object DiagnosticFrameHeader {
    fun readBodyLength(input: InputStream): Long {
        val length = DataInputStream(input).readInt().toLong() and 0xffffffffL
        if (length > 256L) throw ProtocolException("Diagnostic frame exceeds 256-byte envelope")
        return length
    }
}