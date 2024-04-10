package com.example.demo.data.entities

import java.util.*

data class RefreshToken (
    val userID: UUID,
    var refreshToken: String?
)