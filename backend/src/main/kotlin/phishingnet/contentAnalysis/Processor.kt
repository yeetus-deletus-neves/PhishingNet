package phishingnet.contentAnalysis

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysis
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.Occurrences
import phishingnet.contentAnalysis.models.warnings.WarningLog

class Processor(
    private val modules: List<AnalysisModule>,
    private val registeredRisks: List<Risk>
) {

    private val evaluator = EvaluationUnit(registeredRisks)

    /***
     * Goes through all modules evaluating each for the current email
     * receiving a WarningLog and compiling all the received WarningLogs into a final RiskAnalysis for this email
     */
    fun process(email: Email): RiskAnalysis {
        val compiledWarnings = WarningLog(listOf())
        modules.forEach { module ->
            //evaluate module
            val warningLog = module.process(email)
            val from = module.name

            for (warning in warningLog.warnings) {
                val key = warning.key
                val occurrences = warning.value

                if (occurrences.get() == 0) continue

                // Checks if there's already a log with the same type of warning
                val warningAlreadyPresent = compiledWarnings.warnings.keys.contains(key)

                // If there's no such warning, the module result is added to the analysis. If there's already a log with the same
                // warning, the one with the most amount of occurrences is chosen.
                if (!warningAlreadyPresent) {
                    compiledWarnings[key] = occurrences
                }else{
                    val existentWarning = compiledWarnings[key]

                    /***
                     * if the newly found warning as a bigger number of occurrences,
                     * than the previously registered one for the same warning
                     * we update it
                     */
                    if (existentWarning.get() < occurrences.get()) compiledWarnings[key] = occurrences

                }
            }
        }

        val evaluationResult = evaluator.evaluate(compiledWarnings)
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