package phishingnet.api.http.models

data class UserCreateInputModel(
    val username: String,
    val password: String
)

data class TokenInputModel(
    val token: String
)

data class MessageRequest(
    val messageID: String
)