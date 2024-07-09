package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog

/***
 * O retorno deste Módulo é um inteiro com significado correspondente a um booleano
 */
class UrgencyModule : AnalysisModule {
    override val name: String = "Urgency Module"
    override var active: Boolean = false
    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(listOf(Warning.URGENCY))

        val importance = email.importance

        if (importance == "high") warningLog.incrementOccurrences(Warning.URGENCY)

        return warningLog
    }
}