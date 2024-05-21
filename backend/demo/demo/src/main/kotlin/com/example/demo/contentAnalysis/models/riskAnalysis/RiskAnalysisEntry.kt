package com.example.demo.contentAnalysis.models.riskAnalysis

import com.example.demo.contentAnalysis.models.risks.RiskLevel

data class RiskAnalysisEntry (
    val name: String,
    val description: String,
    val threat: RiskLevel
)