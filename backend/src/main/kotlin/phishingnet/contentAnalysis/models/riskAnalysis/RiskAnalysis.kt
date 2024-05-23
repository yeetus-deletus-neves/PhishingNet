package phishingnet.contentAnalysis.models.riskAnalysis

import phishingnet.contentAnalysis.models.risks.RiskLevel

data class RiskAnalysis (
    val threat: RiskLevel,
    val threatJustification: List<RiskAnalysisEntry>,
){
    fun explanation(): String {
        var str = "$threat due to:\n"
        for(entry in this.threatJustification){
            str += "${entry.threat} -> ${entry.name} - ${entry.description}\n"
        }
        return str.dropLast(1)
    }
}