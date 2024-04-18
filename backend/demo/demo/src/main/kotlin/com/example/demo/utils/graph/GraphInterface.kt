package com.example.demo.utils.graph

class GraphInterface {

    private val URL = "https://login.microsoftonline.com/common/oauth2/v2.0/token/"
    private val CLIENT_ID = "cb14d1d3-9a43-4b04-9c52-555211443e63"
    private val GRANT_TYPE = "authorization_code"
    private val SCOPE = "https://graph.microsoft.com/.default"
    private val REDIRECT_URI = "http://localhost:3000/app"
    private val CLIENT_SECRET = "P~N8Q~_.z7Xp6gpIUo~u8N7u6~bS1VpsKEy~1ak1"


    fun getRefresh(token: String): String{
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


        return request.sendRequest()

    }

}