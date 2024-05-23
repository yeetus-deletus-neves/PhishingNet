package phishingnet.api.utils

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

    fun sendRequest(): Response {
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

        return client.newCall(request).execute()
    }



}

enum class HttpMethod {
    GET, POST
}