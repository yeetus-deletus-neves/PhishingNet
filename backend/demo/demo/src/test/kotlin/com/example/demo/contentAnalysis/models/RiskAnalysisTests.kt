package com.example.demo.contentAnalysis.models

import com.example.demo.contentAnalysis.models.riskAnalysis.RiskAnalysis
import com.example.demo.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import com.example.demo.contentAnalysis.models.risks.RiskLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RiskAnalysisTests {
    private val riskAnalysis1 = RiskAnalysis(
        RiskLevel.SHOULD_LOOK_INTO_IT,
        listOf(RiskAnalysisEntry("Test risk", "Nothing to see here", RiskLevel.SHOULD_LOOK_INTO_IT))
    )

    @Test
    fun explanationTest(){
        val expectedExplanation = "Should Look Into It due to:\n" +
                "Should Look Into It -> Test risk - Nothing to see here"

        assertEquals(expectedExplanation, riskAnalysis1.explanation())
    }
}