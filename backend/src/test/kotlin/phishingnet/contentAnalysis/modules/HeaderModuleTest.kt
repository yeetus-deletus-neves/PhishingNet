package phishingnet.contentAnalysis.modules

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phishingnet.contentAnalysis.Processor
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.Requirement
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.testEmail
import phishingnet.contentAnalysis.testEmailWithBadHeaders

class HeaderModuleTest {

    private val mockRisk = Risk(
        "header auth failed",
        "header auth failed",
        RiskLevel.C,
        mutableMapOf(Warning.HEADER_CERTIFICATES_AUTH_FAILED to Requirement(exact = 1))
    )

    private val mockAnalysisEntry =
        RiskAnalysisEntry("header auth failed", "header auth failed", RiskLevel.C)

    private val processor = Processor(listOf(HeaderAuthModule()), listOf(mockRisk))


    @Test
    fun `HeaderModule test for no Threat`() {
        val email = testEmail
        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.A, eval.threat)
        Assertions.assertEquals(0, eval.threatJustification.size)
    }

    @Test
    fun `HeaderModule test for failing certificates`() {
        val email = testEmailWithBadHeaders
        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.C, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }
}