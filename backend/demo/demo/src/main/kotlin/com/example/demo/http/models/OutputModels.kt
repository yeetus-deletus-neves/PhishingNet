package com.example.demo.http.models


data class UserOutputModel(
    val id: String,
    val username: String
)

data class TokenOutputModel(
    val token: String,
    val createdAt: String,
    val lastUsed: String
)

data class MessageOutputModel(
    val message: String
)