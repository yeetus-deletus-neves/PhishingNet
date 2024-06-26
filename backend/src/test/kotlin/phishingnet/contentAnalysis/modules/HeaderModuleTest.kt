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

    private val mockRisk = Risk("header auth failed", "header auth failed", RiskLevel.SUSPICIOUS).apply {
        setRequirement(Warning.HEADER_CERTIFICATES_AUTH_FAILED)
    }

    private val mockAnalysisEntry =
        RiskAnalysisEntry("header auth failed", "header auth failed", RiskLevel.SUSPICIOUS)

    private val processor = Processor(listOf(HeaderAuthModule()), listOf(mockRisk))


    @Test
    fun `HeaderModule test for no Threat`() {
        val email = testEmail
        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.NO_THREAT, eval.threat)
        Assertions.assertEquals(0, eval.threatJustification.size)
    }

    @Test
    fun `HeaderModule test for failing certificates`() {
        val email = testEmailWithBadHeaders
        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.SUSPICIOUS, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }
}