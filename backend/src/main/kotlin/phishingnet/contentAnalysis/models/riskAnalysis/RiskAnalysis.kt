package phishingnet.contentAnalysis.models.riskAnalysis

import kotlinx.serialization.Serializable
import phishingnet.contentAnalysis.models.risks.RiskLevel

@Serializable
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

    //Para efeitos de teste
    override fun toString(): String {
        var str = ""

        str = str.plus("TOTAL THREAT: $threat\n")
        str = str.plus("RISKS FOUND: \n")
        str = str.plus("--------------------------------------------------------------------------------------------\n")
        for (risk in threatJustification){
            str = str.plus("      RISK NAME: ${risk.name}\n")
            str = str.plus("      RISK DESCRIPTION: ${risk.description}\n")
            str = str.plus("      RISK THREAT: ${risk.threat}\n")
            str = str.plus("--------------------------------------------------------------------------------------------\n")
        }

        return str
    }
}