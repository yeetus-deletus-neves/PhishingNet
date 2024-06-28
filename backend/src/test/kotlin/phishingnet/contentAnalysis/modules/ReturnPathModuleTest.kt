package phishingnet.contentAnalysis.modules

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phishingnet.contentAnalysis.Processor
import phishingnet.contentAnalysis.models.Sender
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.Requirement
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.testEmail

class ReturnPathModuleTest {

    private val mockRisk =
        Risk(
            "from and return path differ",
            "from and return path differ",
            RiskLevel.C,
            mutableMapOf(Warning.FROM_DISTINCT_FROM_RETURN_PATH to Requirement(exact = 1))
        )

    private val mockAnalysisEntry =
        RiskAnalysisEntry(
            "from and return path differ",
            "from and return path differ",
            RiskLevel.C
        )

    private val processor = Processor(listOf(ReturnPathModule()), listOf(mockRisk))


    @Test
    fun `ReturnPathModule test for no Threat`() {
        val email = testEmail.copy(from = Sender("1", "email1@test.com"), returnPath = "email1@test.com")
        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.A, eval.threat)
        Assertions.assertEquals(0, eval.threatJustification.size)
    }

    @Test
    fun `ReturnPathModule test for different return path`() {
        val email = testEmail.copy(from = Sender("1", "email1@test.com"), returnPath = "email2@test.com")
        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.C, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }
}