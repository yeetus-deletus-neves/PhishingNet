package phishingnet.contentAnalysis.modules

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phishingnet.contentAnalysis.Processor
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.modules.mock_modules.CountWordsModule
import phishingnet.contentAnalysis.testEmailEmpty

class CountWordsModuleTest {

    private val mockRisk = Risk("To many words", "to many words in email", RiskLevel.MOCK_RISK)
        .apply { setRequirement(Warning.WORD_COUNTED, 5) }

    private val mockAnalysisEntry =
        RiskAnalysisEntry("To many words", "to many words in email", RiskLevel.MOCK_RISK)

    private val processor = Processor(listOf(CountWordsModule()), listOf(mockRisk))

    @Test
    fun `CountWordsModule test for no Threat`() {
        val email = testEmailEmpty.copy(rawBody = "1word")

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.A, eval.threat)
        Assertions.assertEquals(0, eval.threatJustification.size)
    }

    @Test
    fun `CountWordsModule test for less than 5 words`() {
        val email = testEmailEmpty.copy(rawBody = "1word 2word 3word 4word 5word")

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.MOCK_RISK, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }

    @Test
    fun `CountWordsModule test for more than 10 words`() {
        val email = testEmailEmpty.copy(rawBody = "1w 2w 3w 4w 5w 6w 7w 8w 9w 10w 11w")

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.MOCK_RISK, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }
}