package phishingnet.api.utils

import okhttp3.*



class HttpRequest(private var url: String, private var method: HttpMethod) {

    private val client = OkHttpClient()
    private val headers: MutableMap<String, String> = mutableMapOf()
    private var body: FormBody.Builder? = null

    init {
        if (method == HttpMethod.POST) {
            body = FormBody.Builder()
        }
    }

    fun addHeader(name: String, value: String) {
        headers[name] = value
    }

    fun addBody(name: String, value: String) {
        if (method == HttpMethod.POST) {
            body!!.add(name, value)
        }
    }

    fun sendRequest(): Response {
        val requestBuilder = Request.Builder().url(url)

        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }

        if (method == HttpMethod.POST) {
            val builtBody = body!!.build()
            requestBuilder.post(builtBody)
        }

        val request = requestBuilder.build()

        return client.newCall(request).execute()
    }



}

enum class HttpMethod {
    GET, POST
}