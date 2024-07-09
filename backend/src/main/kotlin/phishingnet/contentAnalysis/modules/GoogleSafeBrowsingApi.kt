package phishingnet.contentAnalysis.modules

import okhttp3.*
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody
import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog
import java.io.IOException

data class ThreatEntry(val url: String)
data class ThreatInfo(val threatTypes: List<String>, val platformTypes: List<String>, val threatEntryTypes: List<String>, val threatEntries: List<ThreatEntry>)
data class SafeBrowsingRequest(val client: Map<String, String>, val threatInfo: ThreatInfo)
data class SafeBrowsingResponse(val matches: List<Any>?)

class GoogleSafeBrowsingApi: AnalysisModule {
    override val name = "Google API Module"
    override var active = false

    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(Warning.MALICIOUS_URL)
        warningLog[Warning.MALICIOUS_URL] = 0
        val urls = extractUrlsFromEmailBody(email.body)
        urls.forEach { url ->
            checkUrl(url)
        }

        return warningLog
    }

    private fun extractUrlsFromEmailBody(body: String): List<String>{
        val urlPattern = "https?://[\\w-]+(\\.[\\w-]+)+(/[\\w-./?%&=]*)?"
        val regex = Regex(urlPattern)
        return regex.findAll(body).map { it.value }.toList()
    }

    fun checkUrl(url: String) {
        val apiKey = System.getenv("GOOGLE_SAFE_BROWSING_API") ?: throw IllegalStateException("API_KEY environment variable not set")
        val client = OkHttpClient()
        val gson = Gson()

        val requestBody = SafeBrowsingRequest(
            client = mapOf("clientId" to "yourcompanyname", "clientVersion" to "1.5.2"),
            threatInfo = ThreatInfo(
                threatTypes = listOf("MALWARE", "SOCIAL_ENGINEERING"),
                platformTypes = listOf("ANY_PLATFORM"),
                threatEntryTypes = listOf("URL"),
                threatEntries = listOf(ThreatEntry(url))
            )
        )

        val jsonRequestBody = gson.toJson(requestBody)

        val request = Request.Builder()
            .url("https://safebrowsing.googleapis.com/v4/threatMatches:find?key=$apiKey")
            .post(jsonRequestBody.toRequestBody())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val safeBrowsingResponse = gson.fromJson(responseBody, SafeBrowsingResponse::class.java)
                    if (safeBrowsingResponse.matches.isNullOrEmpty()) {
                        println("The URL is safe.")
                    } else {
                        println("The URL is not safe. Threats found: ${safeBrowsingResponse.matches}")
                    }
                } else {
                    println("Failed to get a response from the Safe Browsing API. Code: ${response.code}")
                }
            }
        })
    }

}

