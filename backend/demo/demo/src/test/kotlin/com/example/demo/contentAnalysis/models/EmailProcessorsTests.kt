package com.example.demo.contentAnalysis.models

import com.example.demo.contentAnalysis.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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

        Assertions.assertEquals(SecVer.PASSED, authDetails.spf)

        Assertions.assertEquals(SecVer.PASSED, authDetails.dkim)
        Assertions.assertEquals(SecVer.PASSED, authDetails.dmarc)
    }

    @Test
    fun `Phishing email Auth details test`() {
        val email = Email("", realPhishingMessageHeaders1)
        val authDetails = email.msgHeadersInfo.authDetails

        Assertions.assertEquals(SecVer.FAILED, authDetails.spf)
        Assertions.assertEquals(SecVer.FAILED, authDetails.dkim)
        Assertions.assertEquals(SecVer.FAILED, authDetails.dmarc)
    }

    @Test
    fun `Clean email body test`() {
        val email = Email(rawBody1, headersExample)

        Assertions.assertEquals(expectedCleanedContent1, email.cleanContent)
    }
    @Test
    fun `Clean email body test real phishingEmail`() {
        val email = Email(realPhishingBody1, headersExample)

        Assertions.assertEquals(cleanRealEmailContent(realPhishingCleanContent1), email.cleanContent)
    }

    //cleans body to remove indentation, new lines
    private fun cleanRealEmailContent(str: String): String = str.trimIndent().replace(Regex("[\\r\\n]+"), " ")


    //TODO(auth details failed test)
}