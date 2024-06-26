package phishingnet.contentAnalysis.models.risks

import phishingnet.contentAnalysis.models.warnings.Warning

data class Risk(
    val name: String,
    val description: String,
    val level: RiskLevel,
    val warningRequirements: MutableMap<Warning, Requirement> = mutableMapOf(),
    val condition: Condition = Condition.INCLUSIVE,
    ) {
    fun setRequirement(
        warning: Warning,
        minOccurrences: Int? = null,
        maxOccurrences: Int? = null,
        exactOccurrences: Int? = null
    ) {
        warningRequirements[warning] = Requirement(minOccurrences, maxOccurrences, exactOccurrences)
    }
}


data class Requirement(val minimum: Int? = null, val maximum: Int? = null, val exact: Int? = null) {
    init {
        require(minimum != null || maximum != null || exact != null) { "Requires at least one input" }
        if (exact != null)
            require(minimum == null && maximum == null) { "Exact is incompatible with minimum and maximum" }
    }
}

enum class Condition {
    EXCLUSIVE,
    INCLUSIVE
}