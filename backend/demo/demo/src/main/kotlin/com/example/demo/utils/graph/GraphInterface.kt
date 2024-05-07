package com.example.demo.utils.graph

import com.example.demo.contentAnalysis.models.MessageHeadersInfo
import com.example.demo.utils.graph.models.ApiMessageHeadersResponse
import com.example.demo.utils.graph.models.ApiRefreshResponse
import com.google.gson.Gson

class GraphInterface {

    private val URL = "https://login.microsoftonline.com/common/oauth2/v2.0/token/"
    private val CLIENT_ID = "cb14d1d3-9a43-4b04-9c52-555211443e63"
    private val GRANT_TYPE = "authorization_code"
    private val SCOPE = "https://graph.microsoft.com/.default"
    private val REDIRECT_URI = "http://localhost:3000/link"
    private val CLIENT_SECRET = "P~N8Q~_.z7Xp6gpIUo~u8N7u6~bS1VpsKEy~1ak1"
    private val GRAPH_BASE_URL = "https://graph.microsoft.com/v1.0/me"


    fun getRefresh(token: String): String?{
        val request = HttpRequest(URL, HttpMethod.POST)

        request.addHeader("Content-Type", "application/x-www-form-urlencoded")
        request.addHeader("Accept", "*/*")
        request.addHeader("Connection", "keep-alive")
        request.addHeader("Accept-Encoding", "gzip, deflate, br")

        request.addBody("code", token)
        request.addBody("client_id", CLIENT_ID)
        request.addBody("grant_type", GRANT_TYPE)
        request.addBody("scope", SCOPE)
        request.addBody("redirect_uri", REDIRECT_URI)
        request.addBody("client_secret", CLIENT_SECRET)


        val response = request.sendRequest()

        return response.use { httpResponse ->
            when (httpResponse.code) {
                400 -> null
                200 -> extractRefreshToken(httpResponse.body?.string()!!)
                else -> throw Exception("HTTP Error: ${httpResponse.code} - ${httpResponse.message}")
            }
        }

    }

    fun getInternetMessageHeaders(conversationId: String, token: String): MessageHeadersInfo? {
        val request = HttpRequest(GRAPH_BASE_URL.plus("/messages/${conversationId}?\$select=internetMessageHeaders"),HttpMethod.GET)
        request.addHeader("Authorization", "Bearer $token")
        val response = request.sendRequest()

        return response.use { httpResponse ->
            when (httpResponse.code){
                400 -> null
                200 -> extractInternetMessageHeaders(httpResponse.body?.string()!!)
                else -> throw Exception("HTTP Error: ${httpResponse.code} - ${httpResponse.message}")
            }
        }

    }

    private fun extractInternetMessageHeaders(responseBody: String): MessageHeadersInfo {
        val serializedResponse = Gson().fromJson(responseBody, ApiMessageHeadersResponse::class.java)
        var authRes:String?= null
        var from:String?= null
        var returnPath:String?= null

        serializedResponse.internetMessageHeaders.forEach { h ->
            if (h.name == "Authentication-Results")
                authRes = h.value
            if (h.name == "From")
                from = h.value
            if (h.name == "Return-Path")
                returnPath = h.value
        }
        if (authRes.isNullOrEmpty() || returnPath.isNullOrEmpty() || from.isNullOrEmpty()) {
            throw IllegalStateException()
        }
        return MessageHeadersInfo(authRes!!, from!!, returnPath!!)

    }

    private fun extractRefreshToken(res: String): String {
        val serializedResponse = Gson().fromJson(res, ApiRefreshResponse::class.java)
        return serializedResponse.refresh_token
    }

}