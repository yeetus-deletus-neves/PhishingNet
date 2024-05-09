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
        val final = mutableMapOf<Risk, ThreatLevel>()  // Mutable map for compilation

        for (analise in list) {
            analise.entries.forEach { analiseEntry ->
                val risk = analiseEntry.key
                val threat = analiseEntry.value

                if (final.containsKey(risk)) {
                    val currentThreat = final[risk]
                    if (currentThreat == null || currentThreat < threat) final[risk] = threat
                } else final[risk] = threat
            }
        }

        return final.toMap()
    }
}