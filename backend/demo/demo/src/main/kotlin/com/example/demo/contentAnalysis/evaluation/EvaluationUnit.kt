package com.example.demo.contentAnalysis.evaluation

import com.example.demo.contentAnalysis.evaluation.models.risks.RiskFound
import com.example.demo.contentAnalysis.evaluation.models.Threat

class EvaluationUnit(private val possibleThreats: List<Threat>) {

    fun evaluate(risksFound: List<RiskFound>): Set<Threat> {
        val threatsFound = mutableSetOf<Threat>()

        for (threat in possibleThreats) {
            val allRequirementsMet = threat.riskRequirements.all { (risk, requiredOccurrences) ->
                val entry = risksFound.find { it.risk == risk }
                entry != null && entry.occurrences() == requiredOccurrences
            }

            if (allRequirementsMet) {
                threatsFound.add(threat)
            }
        }

        return threatsFound
    }
}