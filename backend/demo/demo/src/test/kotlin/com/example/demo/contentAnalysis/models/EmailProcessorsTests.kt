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

    @Test
    fun `Auth details test`(){
        val email = Email("", MessageHeadersInfo("email1","email2",testAuthStr))
        val authDetails = email.msgHeadersInfo.authDetails

        Assertions.assertEquals(SecVer.PASSED, authDetails.spf)

        Assertions.assertEquals(SecVer.PASSED, authDetails.dkim)
        Assertions.assertEquals(SecVer.PASSED, authDetails.dmarc)
    }

    //TODO(auth details failed test)
}