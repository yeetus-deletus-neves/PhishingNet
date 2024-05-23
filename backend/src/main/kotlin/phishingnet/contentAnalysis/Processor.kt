package phishingnet.contentAnalysis

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysis
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.WarningLog

class Processor(
    private val modules: List<AnalysisModule>,
    private val registeredRisks: List<Risk>
) {

    private val evaluator= EvaluationUnit(registeredRisks)

    fun process(email: Email): RiskAnalysis {
        val analysisList = mutableListOf<WarningLog>()
        modules.forEach { module ->
            val result = module.process(email)
            if (result.occurrences() == 0) return@forEach

            // Checks if there's already a log with the same type of warning
            val existentWarning = analysisList.find { it.warning == result.warning }

            // If there's no such warning, the module result is added to the analysis. If there's already a log with the same
            // warning, the one with the most amount of occurrences is chosen.
            if (existentWarning == null) {
                analysisList.add(result)
            }else{
                if (existentWarning.occurrences() < result.occurrences()){
                    analysisList.remove(existentWarning)
                    analysisList.add(result)
                }
            }
        }

        val evaluationResult = evaluator.evaluate(analysisList)
        return compileAnalysis(evaluationResult)
    }

    private fun compileAnalysis(risks: Set<Risk>): RiskAnalysis {
        if (risks.isEmpty()) return RiskAnalysis(RiskLevel.NO_THREAT, listOf())

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