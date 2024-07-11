package phishingnet.contentAnalysis.modules

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phishingnet.contentAnalysis.Processor
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.Requirement
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.testEmailEmpty

class GoogleSafeBrowsingApiTests {

    private val mockRisk = Risk(
        "Test",
        "test",
        RiskLevel.F,
        mutableMapOf(Warning.MALICIOUS_URL to Requirement(1))
    )

    private val mockAnalysisEntry =
        RiskAnalysisEntry("Test", "test", RiskLevel.F)
    private val processor = Processor(listOf(GoogleSafeBrowsingApi()), listOf(mockRisk))


    @Test
    fun `GoogleSafeBrowsingModule test for no Threat`() {
        val email = testEmailEmpty.copy(rawBody = "Use this site https://bit.ly/3L486EI or just click here https://bit.ly/3W22CR9")

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.A, eval.threat)
        Assertions.assertEquals(0, eval.threatJustification.size)
    }

    @Test
    fun `GoogleSafeBrowsingModule test for a Threat`() {
        val email = testEmailEmpty.copy(rawBody = "Use this site http://testsafebrowsing.appspot.com/s/malware.html")

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.F, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }
}