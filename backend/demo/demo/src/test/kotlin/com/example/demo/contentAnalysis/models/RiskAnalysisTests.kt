package com.example.demo.contentAnalysis.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RiskAnalysisTests {
    private val riskAnalysis1 = RiskAnalysisEntry(ThreatLevel.ShouldLookIntoIt, "t")

    @Test
    fun explanationTest(){
        assertEquals("Should Look Into It due to ", riskAnalysis1.explanation())
    }
}