package phishingnet.contentAnalysis

import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.risks.Risk

class EvaluationUnit(private val possibleRisks: List<Risk>) {

    fun evaluate(warningsFound: List<WarningLog>): Set<Risk> {
        val threatsFound = mutableSetOf<Risk>()

        for (risk in possibleRisks) {
            val allRequirementsMet = risk.warningRequirements.all { (warning, requiredOccurrences) ->
                val entry = warningsFound.find { it.warning == warning }
                entry != null && entry.occurrences() >= requiredOccurrences
            }

            if (allRequirementsMet) {
                threatsFound.add(risk)
            }
        }

        return threatsFound
    }
}