package phishingnet.contentAnalysis.models

import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysis
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.RiskLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RiskAnalysisTests {
    private val riskAnalysis1 = RiskAnalysis(
        RiskLevel.B,
        listOf(RiskAnalysisEntry("Test risk", "Nothing to see here", RiskLevel.B))
    )

    @Test
    fun explanationTest(){
            val expectedExplanation = "B due to:\n" +
                "B -> Test risk - Nothing to see here"

        assertEquals(expectedExplanation, riskAnalysis1.explanation())
    }
}