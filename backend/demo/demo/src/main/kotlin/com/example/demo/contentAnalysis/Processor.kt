package com.example.demo.contentAnalysis

import com.example.demo.contentAnalysis.models.*

class Processor(private val modules: List<AnalysisModule>) {

    //change content type
    fun process(email: Email): RiskAnalysis {
        val analysisList = mutableListOf<RiskAnalysis>()
        modules.forEach { module -> analysisList.add(module.process(email)) }

        val finalAnalysis = compileAnalysis(analysisList)

        return finalAnalysis
    }

    private fun compileAnalysis(list: List<RiskAnalysis>): RiskAnalysis {
        val final = mutableMapOf<Risk, RiskAnalysisEntry>()  // Mutable map for compilation

        for (analise in list) {
            analise.entries.forEach { analiseEntry ->
                val risk = analiseEntry.key
                val riskEntry = analiseEntry.value
                val threat = riskEntry.threatLevel
                val justification = riskEntry.threatJustification
                val moduleOfOrigin = riskEntry.moduleOfOrigin

                if (final.containsKey(risk) && final[risk] != null) {
                    val currentThreat = RiskAnalysisEntry(threat, moduleOfOrigin, justification)
                    if (currentThreat.threatLevel < threat) final[risk] = RiskAnalysisEntry(threat, moduleOfOrigin, justification)
                } else final[risk] = RiskAnalysisEntry(threat, moduleOfOrigin, justification)
            }
        }

        return final.toMap()
    }
}