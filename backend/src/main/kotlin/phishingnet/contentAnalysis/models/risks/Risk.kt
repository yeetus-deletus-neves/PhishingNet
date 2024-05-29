package phishingnet.contentAnalysis.models.risks

import phishingnet.contentAnalysis.models.warnings.Warning

data class Risk (
    val name: String,
    val description: String,
    // Warning and minimum number of occurrences
    val level: RiskLevel,
    //val condition: Condition = Condition.INCLUSIVE
){
    val warningRequirements = mutableMapOf<Warning,Int>()

    fun setRequirement(warning: Warning, minOccurrences: Int = 1) {
        warningRequirements[warning] = minOccurrences
    }
}

enum class Condition{
    EXCLUSIVE,
    INCLUSIVE
}