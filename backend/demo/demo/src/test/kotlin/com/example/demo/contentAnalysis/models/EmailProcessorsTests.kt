package com.example.demo.contentAnalysis.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EmailProcessorsTests {

    val testAuthStr = """
        Authentication-Results: spf=pass (sender IP is 209.85.222.48)

        smtp.mailfrom=valimail.com; dkim=pass (signature was verified)

        header.d=valimail.com;dmarc=pass action=none

        header.from=valimail.com;compauth=pass reason=100
    """.trimIndent()

    val rawBody1 =
        """<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body><div dir=\"ltr\">Boa tarde<div><br><div>Este email serve como teste</div></div><div><br></div><div>Cumprimentos</div><div>sender</div></div></body></html>"""
    val expectedCleanedContent1 = "Boa tarde Este email serve como teste Cumprimentos sender"

    @Test
    fun `Auth details test`() {
        val email = Email("", MessageHeadersInfo("email1", "email2", testAuthStr))
        val authDetails = email.msgHeadersInfo.authDetails

        Assertions.assertEquals(SecVer.PASSED, authDetails.spf)

        Assertions.assertEquals(SecVer.PASSED, authDetails.dkim)
        Assertions.assertEquals(SecVer.PASSED, authDetails.dmarc)
    }

    @Test
    fun `Clean email body test`() {
        val email = Email(rawBody1, MessageHeadersInfo("email1", "email2", testAuthStr))

        Assertions.assertEquals(expectedCleanedContent1, email.cleanContent)
    }

    //TODO(auth details failed test)
}