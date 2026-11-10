package livestream.feasibility.mediaprobe

import com.pedro.common.ConnectChecker

class PublisherCallbacks(private val onSuccess: () -> Unit, private val onFailure: () -> Unit, private val onDisconnected: () -> Unit = onFailure) : ConnectChecker {
    override fun onConnectionStarted(url: String) = Unit
    override fun onConnectionSuccess() = onSuccess()
    override fun onConnectionFailed(reason: String) = onFailure()
    override fun onDisconnect() = onDisconnected()
    override fun onAuthError() = onFailure()
    override fun onAuthSuccess() = Unit
}