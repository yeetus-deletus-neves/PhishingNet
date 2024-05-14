package com.example.demo.http.models


data class UserOutputModel(
    val id: String,
    val username: String
)

data class TokenOutputModel(
    val userId: String,
    val email: String?,
    val token: TokenInfo
)

data class TokenInfo(
    val token: String,
    val createdAt: String,
    val lastUsed: String
)

data class MessageOutputModel(
    val message: String
)

data class LinkingOutputModel(
    val email: String?
)