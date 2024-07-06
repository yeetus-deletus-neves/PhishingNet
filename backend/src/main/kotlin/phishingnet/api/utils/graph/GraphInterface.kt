package phishingnet.api.utils.graph

import phishingnet.api.utils.HttpMethod
import phishingnet.api.utils.HttpRequest
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Response
import phishingnet.api.utils.graph.models.*

class GraphInterface {

    private val TOKEN_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/token/"
    private val GRAPH_BASE_URL = "https://graph.microsoft.com/v1.0/me"

    private val CLIENT_ID = "cb14d1d3-9a43-4b04-9c52-555211443e63"
    private val GRANT_TYPE = "authorization_code"
    private val SCOPE = "https://graph.microsoft.com/.default"
    private val REDIRECT_URI = "http://localhost:3000/link"
    private val CLIENT_SECRET = "P~N8Q~_.z7Xp6gpIUo~u8N7u6~bS1VpsKEy~1ak1"


    fun getRefreshFromAccess(token: String): GraphAPITokens?{
        val request = HttpRequest(TOKEN_URL, HttpMethod.POST)

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

        val responseBody = extractBody(
            request.sendRequest()
        ) ?:return null

        return extractTokens(responseBody)
    }

    fun getTokensFromRefresh(refreshToken: String): GraphAPITokens?{
        val request = HttpRequest(TOKEN_URL, HttpMethod.POST)

        request.addHeader("Accept", "*/*")
        request.addHeader("Connection", "keep-alive")
        request.addHeader("Accept-Encoding", "gzip, deflate, br")

        request.addBody("refresh_token", refreshToken)
        request.addBody("client_id", CLIENT_ID)
        request.addBody("grant_type", "refresh_token")
        request.addBody("scope", "$SCOPE, offline_access")
        request.addBody("redirect_uri", REDIRECT_URI)
        request.addBody("client_secret", CLIENT_SECRET)

        val responseBody = extractBody(
            request.sendRequest()
        ) ?:return null

        return extractTokens(responseBody)
    }

    fun getEmailAddress(accessToken: String): String?{
        val request = HttpRequest(GRAPH_BASE_URL, HttpMethod.GET)

        request.addHeader("Authorization", "Bearer $accessToken")

        val responseBody = extractBody(
            request.sendRequest()
        ) ?:return null

        return extractEmail(responseBody)
    }

    fun countSenderHistory(fromAddress: String, token: String): Int? {
        val request = HttpRequest(GRAPH_BASE_URL.plus("/messages?\$filter=(from/emailAddress/address) eq '${fromAddress}'"), HttpMethod.GET)
        request.addHeader("Authorization", "Bearer $token")

        val responseBody = extractBody(
            request.sendRequest()
        ) ?:return null

        return Gson().fromJson(responseBody, GraphMessageResponse::class.java).value.size
    }

    fun getEmailDetails(conversationId: String, token: String): List<GraphEmailDetails>? {
        val result = mutableListOf<GraphEmailDetails>()

        val emailsInfo = getMessageInfo(conversationId, token) ?: return null

        for (email in emailsInfo) {
            val internetHeaders = getMessageInternetHeaders(email.id, token) ?: continue
            val attachments = getAttachments(email.id, token) ?: continue
            result.add(GraphEmailDetails(email, internetHeaders, attachments))
        }

        return result
    }

    private fun getAttachments(id: String,token: String):GraphAttachments?{
        val request = HttpRequest(GRAPH_BASE_URL.plus("/messages/${id}/attachments"), HttpMethod.GET)
        request.addHeader("Authorization", "Bearer $token")

        val responseBody = extractBody(
            request.sendRequest()
        )?:return null

        return Gson().fromJson(responseBody,GraphAttachments::class.java)
    }


    private fun getMessageInfo(conversationId: String, token: String): List<GraphMessage>? {
        val request = HttpRequest(GRAPH_BASE_URL.plus("/messages?\$filter=conversationId eq '${conversationId}'"),
            HttpMethod.GET)
        request.addHeader("Authorization", "Bearer $token")

        val responseBody = extractBody(
            request.sendRequest()
        ) ?:return null

        val extractedFlags = Gson().fromJson(responseBody, GraphMessageResponse::class.java)
        //TODO(Ver como dar handle a conversas com varios emails)
        return extractedFlags.value
    }

    private fun getMessageInternetHeaders(id: String, token: String): GraphInternetHeaders? {
        val request = HttpRequest(GRAPH_BASE_URL.plus("/messages/${id}?\$select=internetMessageHeaders"), HttpMethod.GET)
        request.addHeader("Authorization", "Bearer $token")

        val responseBody = extractBody(
            request.sendRequest()
        ) ?:return null

        //TODO(Ver que headers queremos utilizar)
        return Gson().fromJson(responseBody, GraphInternetHeaders::class.java)
    }

    private fun extractBody(httpResponse: Response): String? {
        return when (httpResponse.code){
            200 -> httpResponse.body?.string()
            400 -> throw IllegalArgumentException("HTTP Error: ${httpResponse.code} - ${httpResponse.message}")
            401 -> throw SecurityException("HTTP Error: ${httpResponse.code} - ${httpResponse.message}")
            404 -> throw NoSuchElementException("HTTP Error: ${httpResponse.code} - ${httpResponse.message}")
            else -> throw Exception("HTTP Error: ${httpResponse.code} - ${httpResponse.message}")
        }
    }

    private fun extractTokens(res: String): GraphAPITokens {
        val serializedResponse = Gson().fromJson(res, ApiTokenResponse::class.java)
        return GraphAPITokens(serializedResponse.access_token, serializedResponse.refresh_token)
    }

    private fun extractEmail(res: String): String{
        val serializedResponse = Gson().fromJson(res, JsonObject::class.java)
        return serializedResponse.get("mail").asString
    }

}
