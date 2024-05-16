package com.example.demo.contentAnalysis

import com.example.demo.contentAnalysis.models.*
import com.example.demo.contentAnalysis.models.riskAnalysis.RiskAnalysis
import com.example.demo.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import com.example.demo.contentAnalysis.models.risks.Risk
import com.example.demo.contentAnalysis.models.risks.RiskLevel
import com.example.demo.contentAnalysis.models.warnings.WarningLog

class Processor(
    private val modules: List<AnalysisModule>,
    private val registeredRisks: List<Risk>
) {

    private val evaluator= EvaluationUnit(registeredRisks)

    //change content type
    fun process(email: Email): RiskAnalysis {
        val analysisList = mutableListOf<WarningLog>()
        modules.forEach { module ->
            val result = module.process(email)
            if (result != null) analysisList.add(result)
        }

        val evaluationResult = evaluator.evaluate(analysisList)
        return compileAnalysis(evaluationResult)
    }

    private fun compileAnalysis(risks: Set<Risk>): RiskAnalysis {
        if (risks.isEmpty()) return RiskAnalysis(RiskLevel.NoThreat, listOf())

        val sortedRisks = risks.sortedByDescending { it.level.level }

        val maxThreat = sortedRisks[0].level
        val compiledRisks: MutableList<RiskAnalysisEntry> = mutableListOf()

        for (toCompile in sortedRisks) {
            val compiled = RiskAnalysisEntry(
                toCompile.name,
                toCompile.description,
                toCompile.level
            )
            compiledRisks.add(compiled)
        }

        return RiskAnalysis(maxThreat, compiledRisks)
    }
}