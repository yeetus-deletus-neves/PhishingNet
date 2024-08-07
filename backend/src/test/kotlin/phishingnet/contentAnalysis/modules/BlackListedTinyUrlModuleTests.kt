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

class BlackListedTinyUrlModuleTests {

    private val mockRisk = Risk(
        "Urls are shortened",
        "we can't predict were you're going to be redirected to",
        RiskLevel.B,
        mutableMapOf(Warning.URL_SHORTENED to Requirement(1))
    )

    private val mockAnalysisEntry =
        RiskAnalysisEntry(
            "Urls are shortened",
            "we can't predict were you're going to be redirected to",
            RiskLevel.B
        )

    private val processor = Processor(listOf(BlackListedTinyUrlModule()), listOf(mockRisk))

    @Test
    fun `BlackListedTinyUrlModule test`() {
        val email = testEmailEmpty.copy(rawBody = "Use this site https://bit.ly/3L486EI or just click here https://bit.ly/3W22CR9")

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.B, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))

    }
}