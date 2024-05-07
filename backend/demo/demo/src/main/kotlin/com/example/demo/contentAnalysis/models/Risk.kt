package com.example.demo.contentAnalysis.models

data class Risk (
    val name: String,
    val value: Int
)

data class MessageHeadersInfo(
    val from: String,
    val returnPath: String,
    val authenticationResults: String
)