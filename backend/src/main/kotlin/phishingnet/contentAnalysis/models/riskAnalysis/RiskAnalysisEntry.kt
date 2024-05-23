package phishingnet.contentAnalysis.models.riskAnalysis

import phishingnet.contentAnalysis.models.risks.RiskLevel

data class RiskAnalysisEntry (
    val name: String,
    val description: String,
    val threat: RiskLevel
)