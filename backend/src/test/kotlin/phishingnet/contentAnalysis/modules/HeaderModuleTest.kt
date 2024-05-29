package phishingnet.contentAnalysis.modules

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phishingnet.contentAnalysis.Processor
import phishingnet.contentAnalysis.models.Sender
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.testEmail
import phishingnet.contentAnalysis.testEmailWithBadHeaders

class HeaderModuleTest {


    @Test
    fun `HeaderModule test for no Threat`(){
        val email = testEmail.copy(from = Sender("1", "email1@test.com"), returnPath = "email1@test.com")
        val headerModule = HeaderModule()
        val mockRisk = Risk("from and return path differ", "from and return path differ", RiskLevel.SUSPICIOUS)
        mockRisk.setRequirement(Warning.FROM_DISTINCT_FROM_RETURN_PATH)
        mockRisk.setRequirement(Warning.HEADER_CERTIFICATES_AUTH_FAILED)

        val process = Processor(listOf(headerModule), listOf(mockRisk))
        val eval = process.process(email)

        Assertions.assertEquals(RiskLevel.NO_THREAT, eval.threat)
        Assertions.assertEquals(0, eval.threatJustification.size)
    }

    @Test
    fun `HeaderModule test for different return path and from with passing certificates`(){
        val email = testEmail.copy(from = Sender("1", "email1@test.com"), returnPath = "email2@test.com")
        val headerModule = HeaderModule()
        val mockRisk = Risk("from and return path differ", "from and return path differ", RiskLevel.SUSPICIOUS)
        mockRisk.setRequirement(Warning.FROM_DISTINCT_FROM_RETURN_PATH)
        mockRisk.setRequirement(Warning.HEADER_CERTIFICATES_AUTH_FAILED)

        val process = Processor(listOf(headerModule), listOf(mockRisk))
        val eval = process.process(email)

        Assertions.assertEquals(RiskLevel.NO_THREAT, eval.threat)
        Assertions.assertEquals(0, eval.threatJustification.size)
    }

    @Test
    fun `HeaderModule test for different return path and failing certificates`(){
        val email = testEmailWithBadHeaders.copy(from = Sender("1", "email1@test.com"), returnPath = "email2@test.com")
        val headerModule = HeaderModule()
        val mockRisk = Risk("from and return path differ", "from and return path differ", RiskLevel.SUSPICIOUS)
        mockRisk.setRequirement(Warning.FROM_DISTINCT_FROM_RETURN_PATH)
        mockRisk.setRequirement(Warning.HEADER_CERTIFICATES_AUTH_FAILED)

        val process = Processor(listOf(headerModule), listOf(mockRisk))
        val eval = process.process(email)

        val mockAnalysisEntry = RiskAnalysisEntry("from and return path differ", "from and return path differ", RiskLevel.SUSPICIOUS)

        Assertions.assertEquals(RiskLevel.SUSPICIOUS, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }
}