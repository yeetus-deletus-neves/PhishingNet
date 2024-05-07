package com.example.demo.contentAnalysis.models

//TODO set MessageHeadersInfo as simple parameters of email
data class Email(
    val body: String,
    val msgHeadersInfo: MessageHeadersInfo
) {
    val cleanContent by lazy { cleanContent(body) }
}

data class MessageHeadersInfo(
    val from: String,
    val returnPath: String,
    val authenticationResults: String
)

private fun cleanContent(content: String): String {
    //TODO()
    return content
}