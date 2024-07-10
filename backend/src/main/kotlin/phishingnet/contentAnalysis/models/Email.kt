package phishingnet.contentAnalysis.models
import org.jsoup.Jsoup


data class Email(
    val from: Sender,
    val fromEmailCount: Int,
    val sender: Sender,
    val subject: String,
    val importance: String,
    val hasAttachments: Boolean,
    val isRead: Boolean,
    val returnPath: String?,
    private val rawAuthResults: String?,
    private val rawBody: String,
    val attachments: List<String>
) {
    val body: String = Jsoup.parse(rawBody).text()
    val authDetails: AuthDetails by lazy { processAuth(rawAuthResults, "Microsoft") }
}

data class Sender(
    val name: String,
    val address: String
)

//implementation removes \n from the body, and indentation
enum class SecurityVerification {
    PASSED, FAILED, IGNORED
}

data class AuthDetails(val spf: SecurityVerification, val dkim: SecurityVerification, val dmarc: SecurityVerification)

fun processAuth(authResults: String?, provider: String): AuthDetails {
    require(provider == "Microsoft") { "Current Implementation only supports Microsoft" }
    if (authResults == null) return AuthDetails(SecurityVerification.IGNORED, SecurityVerification.IGNORED, SecurityVerification.IGNORED)
    when(provider) {
        "Microsoft" -> return processAuthMicrosoft(authResults)
        "Google" -> throw IllegalArgumentException("Implementation Not Done For Google email Provider")
        "Yahoo" -> throw IllegalArgumentException("Implementation Not Done For Yahoo email Provider")
        else -> throw IllegalArgumentException("Invalid Provider, or not implemented email Provider")
    }
}

fun processAuthMicrosoft(authResults: String): AuthDetails {
    val authMap: MutableMap<String, SecurityVerification> = mutableMapOf("spf" to SecurityVerification.FAILED, "dkim" to SecurityVerification.FAILED, "dmarc" to SecurityVerification.FAILED)
    val map = cleanAuthResults(authResults)

    map.forEach { (key, value) ->
        if (value.startsWith("pass"))
            when (key) {
                "spf", "dkim", "dmarc" -> authMap[key] = SecurityVerification.PASSED
            }
    }

    return AuthDetails(authMap["spf"]!!, authMap["dkim"]!!, authMap["dmarc"]!!)
}

fun cleanAuthResults(authResults: String): Map<String, String> {
    val cleanAuthResultsMap = mutableMapOf<String, String>()
    val authResultsInLine = authResults.trim().replace("\n", "").replace("Authentication-Results:","")

    val parts = authResultsInLine.split(";")
    parts.forEach {  part ->
        val trimmedPart = part.trim()
        if (trimmedPart.isNotEmpty()) {
            val keyValue = trimmedPart.split("=", limit = 2)
            val key = keyValue[0].trim()
            val value = keyValue[1].trim()
            if (key.isNotEmpty() && value.isNotEmpty()) cleanAuthResultsMap[key] = value
        }
    }
    return cleanAuthResultsMap
}

