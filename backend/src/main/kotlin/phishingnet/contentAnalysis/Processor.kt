package phishingnet.contentAnalysis

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysis
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.WarningLog

class Processor(private val modules: List<AnalysisModule>, registeredRisks: List<Risk>) {

    private val evaluator = EvaluationUnit(registeredRisks)

    fun process(emails: List<Email>): RiskAnalysis {
        val risksIdentified = mutableSetOf<Risk>()

        for (email in emails) {
            processSingleEmail(email).forEach{
                risksIdentified.add(it)
            }
        }

        return compileAnalysis(risksIdentified)
    }

    /***
     * Percorre todos os módulos avaliando cada um para o email atual
     * recebe um WarningLog por email e compila todos os WarningLogs recebidos numa RiskAnalysis final para esta conversa de emails
     *
     */
    private fun processSingleEmail(email: Email): Set<Risk> {
        val compiledWarnings = WarningLog(listOf())
        modules.forEach { module ->

            //avalia módulo
            val warningLog = module.process(email)

            for (warning in warningLog.warnings) {
                val key = warning.key
                val occurrences = warning.value

                if (occurrences == 0) continue

                // Verifica se já existe algum warningLog com o mesmo tipo de warning
                val warningAlreadyPresent = compiledWarnings.warnings.keys.contains(key)

                /***
                 * Se não existir o warning, o resultado do módulo é adicionado à análise.
                 * Caso já exista um warning com o mesmo aviso, é escolhido aquele que tiver maior número de ocorrências
                 */
                if (!warningAlreadyPresent) compiledWarnings[key] = occurrences
                else{
                    val existentWarningOccurrences = compiledWarnings[key]
                    if (existentWarningOccurrences < occurrences) compiledWarnings[key] = occurrences
                }
            }
        }

        return evaluator.evaluate(compiledWarnings)
    }

    private fun compileAnalysis(risks: Set<Risk>): RiskAnalysis {
        if (risks.isEmpty()) return RiskAnalysis(RiskLevel.A, listOf())

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