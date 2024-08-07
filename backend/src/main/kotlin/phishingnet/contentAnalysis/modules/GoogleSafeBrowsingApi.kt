package phishingnet.contentAnalysis.modules

import okhttp3.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.Sender
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog
import java.io.IOException
import kotlin.jvm.Throws

data class ThreatEntry(val url: String)
data class ThreatInfo(
    val threatTypes: List<String>,
    val platformTypes: List<String>,
    val threatEntryTypes: List<String>,
    val threatEntries: List<ThreatEntry>
)

data class SafeBrowsingRequest(val client: Map<String, String>, val threatInfo: ThreatInfo)
data class SafeBrowsingResponse(val matches: List<Any>?)

class GoogleSafeBrowsingApi : AnalysisModule {
    override val name = "Módulo Google Safe API"

    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(Warning.MALICIOUS_URL)
        warningLog[Warning.MALICIOUS_URL] = 0
        val urls = extractUrlsFromEmailBodyToThreats(email.body)
        try {
            warningLog[Warning.MALICIOUS_URL] = checkUrls(urls)
        } catch (ex: Exception) {
            println("Modulo não foi avaliado devido a ${ex.message}")
        }
        return warningLog
    }

    private fun extractUrlsFromEmailBodyToThreats(body: String): List<ThreatEntry> {
        val urlPattern = "https?://[\\w-]+(\\.[\\w-]+)+(/[\\w-./?%&=]*)?"
        val regex = Regex(urlPattern)
        return regex.findAll(body).map { ThreatEntry(it.value) }.toList()
    }

    @Throws(IllegalStateException::class)
    fun checkUrls(urls: List<ThreatEntry>): Int {
        var occurrences = 0
        val apiKey = System.getenv("GOOGLE_SAFE_BROWSING_API")
            ?: throw IllegalStateException("chave da api google safe browsing não se encontra corretamente configurada")
        val client = OkHttpClient()
        val gson = Gson()

        val requestBody = SafeBrowsingRequest(
            client = mapOf("clientId" to "phishing net", "clientVersion" to "1.5.2"),
            threatInfo = ThreatInfo(
                threatTypes = listOf("MALWARE", "SOCIAL_ENGINEERING"),
                platformTypes = listOf("ANY_PLATFORM"),
                threatEntryTypes = listOf("URL"),
                threatEntries = urls
            )
        )

        val jsonRequestBody = gson.toJson(requestBody)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url("https://safebrowsing.googleapis.com/v4/threatMatches:find?key=$apiKey")
            .post(jsonRequestBody.toRequestBody(mediaType))
            .build()

        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val safeBrowsingResponse = gson.fromJson(responseBody, SafeBrowsingResponse::class.java)
                if (safeBrowsingResponse.matches == null) 0 else safeBrowsingResponse.matches.size
            } else {
                0
            }
        } catch (e: IOException) {
            e.printStackTrace()
            0
        }
    }
}
