package phishingnet.contentAnalysis.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phishingnet.contentAnalysis.*

class EmailProcessorsTests {

    private val headersExample = MessageHeadersInfo("\"1\" <email1>", "email1", testAuthStr)
    private val realPhishingMessageHeaders1 = MessageHeadersInfo(
        "\"Stansted Airport College Enquiry\" <admissions@harlow-college.ac.uk>",
        "admissions@harlow-college.ac.uk",
        realPhishingHeaders1
    )
    @Test
    fun `Auth details test`() {
        val email = Email("", headersExample)
        val authDetails = email.msgHeadersInfo.authDetails

        Assertions.assertEquals(SecurityVerification.PASSED, authDetails.spf)

        Assertions.assertEquals(SecurityVerification.PASSED, authDetails.dkim)
        Assertions.assertEquals(SecurityVerification.PASSED, authDetails.dmarc)
    }

    @Test
    fun `Phishing email Auth details test`() {
        val email = Email("", realPhishingMessageHeaders1)
        val authDetails = email.msgHeadersInfo.authDetails

        Assertions.assertEquals(SecurityVerification.FAILED, authDetails.spf)
        Assertions.assertEquals(SecurityVerification.FAILED, authDetails.dkim)
        Assertions.assertEquals(SecurityVerification.FAILED, authDetails.dmarc)
    }

    @Test
    fun `Clean email body test`() {
        val email = Email(rawBody1, headersExample)

        Assertions.assertEquals(expectedCleanedContent1, email.body)
    }
    @Test
    fun `Clean email body test real phishingEmail`() {
        val email = Email(realPhishingBody1, headersExample)

        Assertions.assertEquals(cleanRealEmailContent(realPhishingCleanContent1), email.body)
    }

    //cleans body to remove indentation, new lines
    private fun cleanRealEmailContent(str: String): String = str.trimIndent().replace(Regex("[\\r\\n]+"), " ")


    //TODO(auth details failed test)
}