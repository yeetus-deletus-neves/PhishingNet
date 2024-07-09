package phishingnet.contentAnalysis

import phishingnet.contentAnalysis.models.risks.Condition
import phishingnet.contentAnalysis.models.risks.Requirement
import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.risks.Risk

class EvaluationUnit(private val possibleRisks: List<Risk>) {

    /***
     * Checks if all requirements are met to consider a risk exists
     * by requirements we consider the that every warning is present
     * and its occurrences match the required occurrences
     */
    fun evaluate(warningsFound: WarningLog): Set<Risk> {
        val threatsFound = mutableSetOf<Risk>()

        for (risk in possibleRisks) {
            val allRequirementsMet =
                if (risk.condition == Condition.INCLUSIVE) risk.warningRequirements.all { (warning, requirement) ->
                    //if(!warningsFound.warnings.containsKey(warning)) false else
                    occurrencesMatchRequirement(warningsFound.warnings[warning] ?: 0, requirement)
                } else risk.warningRequirements.any { (warning, requirement) ->
                    //if(!warningsFound.warnings.containsKey(warning)) false else
                    occurrencesMatchRequirement(warningsFound.warnings[warning] ?: 0, requirement)
                }
            if (allRequirementsMet) threatsFound.add(risk)
        }
        return threatsFound
    }

    private fun occurrencesMatchRequirement(occurrences: Int, requirement: Requirement): Boolean = when {
        requirement.exact != null -> occurrences == requirement.exact
        requirement.minimum != null && requirement.maximum == null -> occurrences >= requirement.minimum
        requirement.minimum == null && requirement.maximum != null -> occurrences <= requirement.maximum
        requirement.minimum != null && requirement.maximum != null ->
            occurrences >= requirement.minimum && occurrences <= requirement.maximum
        else -> false //TODO TEST IF ITS REACHABLE AND MAYBE THROW EXCEPTION
    }
}