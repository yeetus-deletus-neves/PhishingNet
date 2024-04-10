package com.example.demo.data.entities

import java.util.UUID

data class UserToken (
    val userID: UUID,
    val userToken: String
)