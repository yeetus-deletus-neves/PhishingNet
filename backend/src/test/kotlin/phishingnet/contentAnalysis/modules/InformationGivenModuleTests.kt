package phishingnet.contentAnalysis.modules

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phishingnet.contentAnalysis.Processor
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.testEmail

class InformationGivenModuleTests {

    private val mockRisk =
        Risk("Gives information", "gives contact or iban information", RiskLevel.SUSPICIOUS)
            .apply { setRequirement(Warning.ASKS_FOR_SENSITIVE_INFO) }

    private val mockAnalysisEntry =
        RiskAnalysisEntry("Gives information", "gives contact or iban information", RiskLevel.SUSPICIOUS)

    private val processor = Processor(listOf(InformationGivenModule()), listOf(mockRisk))

    @Test
    fun `InformationGivenModule test for no Threat`() {
        val email = testEmail.copy(rawBody = "test no suspect info being asked here")

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.NO_THREAT, eval.threat)
        Assertions.assertEquals(0, eval.threatJustification.size)
    }

    @Test
    fun `InformationGivenModule test for giving a number`() {
        val email = testEmail.copy(rawBody = "call this number +1234567890")

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.SUSPICIOUS, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }

    @Test
    fun `InformationGivenModule test for giving a iban`() {
        val email = testEmail.copy(rawBody = "send payment to this iban PT89370400440532013000")

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.SUSPICIOUS, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }

    @Test
    fun `InformationGivenModule test for giving a iban and a number`() {
        val email = testEmail.copy(
            rawBody = "send payment to this iban PT50000201231234567890154 for any question call this number +1234567890"
        )

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.SUSPICIOUS, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }
}