package livestream.feasibility.mediaprobe

import android.app.Activity

object YouTubeAuthorizationDiagnostic {
    fun resultMessage(resultCode: Int, hasData: Boolean): String =
        if (resultCode == Activity.RESULT_CANCELED && !hasData)
            "YouTube authorization ended without a result. This does not establish why. Publishing remains disabled."
        else "YouTube authorization not completed. Publishing remains disabled."
}