package com.example.demo.contentAnalysis.models

import com.example.demo.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import com.example.demo.contentAnalysis.models.risks.RiskLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RiskAnalysisTests {
    private val riskAnalysis1 = RiskAnalysisEntry("Test risk", "Nothing to see here", RiskLevel.ShouldLookIntoIt)

    @Test
    fun explanationTest(){
        //assertEquals("Should Look Into It due to ", riskAnalysis1.explanation())
    }
}