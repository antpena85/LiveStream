package livestream.feasibility.mediaprobe

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import kotlin.math.ceil

/** Local diagnostic renderer only; never a broadcast fit authorization. */
object DiagnosticOverlay {
    fun render(text: String): Bitmap? {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE; textSize = 32f; typeface = Typeface.MONOSPACE }
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val left = 16f - minOf(0, bounds.left)
        val required = ceil(left + maxOf(paint.measureText(text), bounds.right.toFloat()) + 16f).toInt()
        if (required > 960 || bounds.top < -72 || bounds.bottom > 56) return null
        return Bitmap.createBitmap(maxOf(480, required), 128, Bitmap.Config.ARGB_8888).apply {
            val canvas = Canvas(this)
            canvas.drawColor(Color.argb(235, 0, 0, 0))
            canvas.drawText(text, left, 72f, paint)
        }
    }
}