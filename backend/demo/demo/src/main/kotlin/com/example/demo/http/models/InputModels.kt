package com.example.demo.http.models

data class UserCreateInputModel(
    val username: String,
    val password: String
)

data class TokenInputModel(
    val token: String
)

data class ContentInputModel(
    val content: String
)