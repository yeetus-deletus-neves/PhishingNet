package phishingnet.contentAnalysis.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phishingnet.contentAnalysis.*

class EmailProcessorsTests {

    @Test
    fun `Auth details test`() {
        val authDetails = testEmailEmpty.authDetails

        Assertions.assertEquals(SecurityVerification.PASSED, authDetails.spf)
        Assertions.assertEquals(SecurityVerification.PASSED, authDetails.dkim)
        Assertions.assertEquals(SecurityVerification.PASSED, authDetails.dmarc)
    }

    @Test
    fun `Phishing email Auth details test`() {
        val authDetails = realPhishingEmail1.authDetails

        Assertions.assertEquals(SecurityVerification.FAILED, authDetails.spf)
        Assertions.assertEquals(SecurityVerification.FAILED, authDetails.dkim)
        Assertions.assertEquals(SecurityVerification.FAILED, authDetails.dmarc)
    }

    @Test
    fun `Clean email body test`() {
        val email = testEmail

        Assertions.assertEquals(expectedCleanedContent1, email.body)
    }
    @Test
    fun `Clean email body test real phishingEmail`() {
        val email = realPhishingEmail1

        Assertions.assertEquals(cleanRealEmailContent(realPhishingCleanContent1), email.body)
    }

    //cleans body to remove indentation, new lines
    private fun cleanRealEmailContent(str: String): String = str.trimIndent().replace(Regex("[\\r\\n]+"), " ")


    //TODO(auth details failed test)
}