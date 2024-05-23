package phishingnet.api.security

import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.*

@Component
class TokenEncoder {

    fun encode(token: String) = hash(token)

    fun validate(validationInfo: String, token: String): Boolean =
        validationInfo == hash(token)

    private fun hash(input: String): String {
        val messageDigest = MessageDigest.getInstance("SHA256")
        return Base64.getUrlEncoder().encodeToString(
            messageDigest.digest(
                Charsets.UTF_8.encode(input).array()
            )
        )
    }
}