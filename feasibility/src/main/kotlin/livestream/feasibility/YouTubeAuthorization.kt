package livestream.feasibility

enum class AuthorizationResponse { Cancelled }
enum class YouTubeAuthorizationResult { NotAuthorized }

object YouTubeAuthorization {
    fun complete(response: AuthorizationResponse, proceed: () -> Unit): YouTubeAuthorizationResult =
        when (response) {
            AuthorizationResponse.Cancelled -> YouTubeAuthorizationResult.NotAuthorized
        }
}