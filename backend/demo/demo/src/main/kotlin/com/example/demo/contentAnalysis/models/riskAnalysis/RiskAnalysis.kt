package com.example.demo.contentAnalysis.models.riskAnalysis

import com.example.demo.contentAnalysis.models.risks.RiskLevel

data class RiskAnalysis (
    val threat: RiskLevel,
    val threatJustification: List<RiskAnalysisEntry>,
)