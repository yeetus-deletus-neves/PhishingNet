package phishingnet.contentAnalysis.models.riskAnalysis

import kotlinx.serialization.Serializable
import phishingnet.contentAnalysis.models.risks.RiskLevel

@Serializable
data class RiskAnalysisEntry (
    val name: String,
    val description: String,
    val threat: RiskLevel,
)