package com.example.demo.data.entities

import java.util.UUID

data class User (
    val userID: UUID,
    val username: String,
    val password: String
)