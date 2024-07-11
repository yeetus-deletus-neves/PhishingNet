package phishingnet.contentAnalysis

import phishingnet.contentAnalysis.models.risks.Condition
import phishingnet.contentAnalysis.models.risks.Requirement
import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.risks.Risk

class EvaluationUnit(private val possibleRisks: List<Risk>) {

    /***
     * Verifica se todos os requisitos foram cumpridos para considerar que um risco existe
     * por cumprimento dos requisitos consideramos, no caso da condition ser INCLUSIVE:
     *  -> presença de todos os warnings, e os valores de ocorrências necessários para os mesmos se encontrarem como ativos,
     * caso a condition seja EXCLUSIVE:
     *  -> presença de pelo menos um warning, e os valores de ocorrências necessários para os mesmo se encontrar ativo.
     */
    fun evaluate(warningsFound: WarningLog): Set<Risk> {
        val threatsFound = mutableSetOf<Risk>()

        for (risk in possibleRisks) {
            val allRequirementsMet =
                if (risk.condition == Condition.INCLUSIVE) risk.warningRequirements.all { (warning, requirement) ->
                    occurrencesMatchRequirement(warningsFound.warnings[warning] ?: 0, requirement)
                } else risk.warningRequirements.any { (warning, requirement) ->
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
        else -> false //TODO testar se é alcançavel e talvez atirar excepção?
    }
}