package com.example.demo.http.models

data class UserCreateInputModel(
    val username: String,
    val password: String
)

data class UserHomeOutputModel(
    val id: String,
    val username: String
)