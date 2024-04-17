package com.example.demo.utils.graph

import com.example.demo.utils.graph.models.ApiRefreshResponse
import com.google.gson.Gson
import okhttp3.*

class HttpRequest(private var url: String, private var method: HttpMethod) {

    private val client = OkHttpClient()
    private val headers: MutableMap<String, String> = mutableMapOf()
    private val body: MutableMap<String, String> = mutableMapOf()

    fun addHeader(name: String, value: String) {
        headers[name] = value
    }

    fun addBody(name: String, value: String) {
        if (method == HttpMethod.POST) {
            body[name] = value
        }
    }

    fun sendRequest(): String{
        val requestBuilder = Request.Builder().url(url)

        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }

        if (method == HttpMethod.POST) {
            val formBuilder = FormBody.Builder()
            for ((key, value) in body) {
                formBuilder.add(key, value)
            }
            requestBuilder.post(formBuilder.build())
        }

        val request = requestBuilder.build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw Exception("Request for refresh token failed:\n ${response.body?.string()}")

        return extractRefreshToken(response.body!!.string())
    }

    private fun extractRefreshToken(res: String): String {
        val serializedResponse = Gson().fromJson(res, ApiRefreshResponse::class.java)
        return serializedResponse.refresh_token
    }

}

enum class HttpMethod {
    GET, POST
}