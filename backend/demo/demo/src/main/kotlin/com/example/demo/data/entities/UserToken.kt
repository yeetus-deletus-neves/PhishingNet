package com.example.demo.data.entities

import java.time.Instant
import java.util.UUID

data class UserToken (
    val userID: UUID,
    val userToken: String,
    val createdAt: Instant,
    val lastUsed: Instant
)