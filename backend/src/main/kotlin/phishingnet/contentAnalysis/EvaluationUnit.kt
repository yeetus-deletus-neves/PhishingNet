package phishingnet.contentAnalysis

import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.risks.Risk

class EvaluationUnit(private val possibleRisks: List<Risk>) {

    /***
     * Checks if all requirements are met to consider a risk exists
     * by requirements we consider the that every warning is present
     * and its occurrences equal or excel the set value for minimum occurrences
     */
    fun evaluate(warningsFound: WarningLog): Set<Risk> {
        val threatsFound = mutableSetOf<Risk>()

        for (risk in possibleRisks) {
            val allRequirementsMet = risk.warningRequirements.all { (warning, requiredOccurrences) ->
                val warningIsPresent = warningsFound.warnings.containsKey(warning)
                val occurrences = warningsFound.warnings[warning] ?: 0
                warningIsPresent && occurrences >= requiredOccurrences
            }

            if (allRequirementsMet)  threatsFound.add(risk)
        }

        return threatsFound
    }
}