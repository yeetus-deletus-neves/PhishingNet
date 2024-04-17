package com.example.demo.utils.graph.models

data class ApiRefreshResponse (
    val token_type: String,
    val scope: String,
    val expires_in: Int,
    val ext_expires_in: Int,
    val access_token: String,
    val refresh_token: String,
)