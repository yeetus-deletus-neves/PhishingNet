package phishingnet.api.security

import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

@Component
class SaltPepperEncoder {

    //valor Pepper para todas as contas do serviço
    private val PEPPER = "C*eRYczkr5Lci&3n4F5R*Lc5LsNi8@Mot%#!^Wg"

    fun encode(value: String): HashResult {
        val random = SecureRandom()
        val byteSalt = ByteArray(64)
        random.nextBytes(byteSalt)
        val stringSalt: String = Base64.getEncoder().encodeToString(byteSalt)

        return HashResult(hash(value, stringSalt), stringSalt)
    }

    fun validate(value: String, salt: String, encodedValue: String): Boolean =
        encodedValue == hash(value, salt)

    private fun hash(input: String, salt: String): String {
        val message = input.plus(salt).plus(PEPPER)
        val messageDigest = MessageDigest.getInstance("SHA256")
        return Base64.getUrlEncoder().encodeToString(
            messageDigest.digest(
                Charsets.UTF_8.encode(message).array()
            )
        )
    }
     data class HashResult(val encodedValue: String, val salt: String)
}