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

class IbanDetectionModuleTests {

    private val mockRisk =
        Risk(
            "Gives information",
            "gives contact or iban information",
            RiskLevel.C,
            mutableMapOf(Warning.ASKS_FOR_IBAN to Requirement(exact = 1))
        )

    private val mockAnalysisEntry =
        RiskAnalysisEntry("Gives information", "gives contact or iban information", RiskLevel.C)

    private val processor = Processor(listOf(IbanDetectionModule()), listOf(mockRisk))

    @Test
    fun `IbanDetectionModule test for no Threat`() {
        val email = testEmail.copy(rawBody = "test no suspect info being asked here")

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.A, eval.threat)
        Assertions.assertEquals(0, eval.threatJustification.size)
    }

    @Test
    fun `IbanDetectionModule test for giving a iban`() {
        val email = testEmail.copy(rawBody = "send payment to this iban PT89370400440532013000")

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.C, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }
}