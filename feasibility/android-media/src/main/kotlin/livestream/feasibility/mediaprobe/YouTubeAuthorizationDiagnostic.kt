package livestream.feasibility.mediaprobe

import android.app.Activity

object YouTubeAuthorizationDiagnostic {
    fun providerErrorMessage(code: Int): String = "YouTube authorization failed (Google status $code). Publishing remains disabled."
    fun resultMessage(resultCode: Int, hasData: Boolean): String =
        if (resultCode == Activity.RESULT_CANCELED && !hasData)
            "YouTube authorization ended without a result. This does not establish why. Publishing remains disabled."
        else if (resultCode != Activity.RESULT_OK && hasData)
            "YouTube authorization returned a non-success result with data. The cause is unknown. Publishing remains disabled."
        else "YouTube authorization not completed. Publishing remains disabled."
}