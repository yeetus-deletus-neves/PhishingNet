package phishingnet.contentAnalysis.models
import org.jsoup.Jsoup


data class Email(
    val from: Sender,
    val sender: Sender,
    val subject: String,
    val importance: String,
    val hasAttachments: Boolean,
    val isRead: Boolean,
    val returnPath: String,

    private val rawAuthResults: String,
    private val rawBody: String
) {
    val body: String
    val authDetails: AuthDetails by lazy { processAuth(rawAuthResults, "Microsoft") }

    init {
        require(returnPath.isNotEmpty()) { "returnPath cannot be empty" }
        require(rawAuthResults.isNotEmpty()) { "authenticationResults cannot be empty" }
        body = cleanContent(rawBody)
    }

    private fun cleanContent(content: String): String {
        return Jsoup.parse(content).text()
    }
}

data class Sender(
    val name: String,
    val address: String
)

//TODO add provider parameter
data class MessageHeadersInfo(
    val from: String,
    val returnPath: String,
    val authenticationResults: String
){
    init {
        require(from.isNotEmpty()) { "from cannot be empty" }
        require(returnPath.isNotEmpty()) { "returnPath cannot be empty" }
        require(authenticationResults.isNotEmpty()) { "authenticationResults cannot be empty" }
        //require(from.contains('<')&&from.contains('>')) { "invalid from format" }
    }

    val authDetails by lazy { processAuth(authenticationResults, "Microsoft") }
}

//implementation removes \n from the body, and indentation
enum class SecurityVerification {
    PASSED, FAILED
}

data class AuthDetails(val spf: SecurityVerification, val dkim: SecurityVerification, val dmarc: SecurityVerification)

fun processAuth(authResults: String, provider: String): AuthDetails {
    require(provider == "Microsoft") { "Current Implementation only supports Microsoft" }
    when(provider) {
        "Microsoft" -> return processAuthMicrosoft(authResults)
        "Google" -> TODO()
        "Yahoo" -> TODO()
        else -> throw IllegalArgumentException("Invalid Provider")
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

