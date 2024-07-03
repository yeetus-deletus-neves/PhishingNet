package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog

class ImportanceAnalysis: AnalysisModule {
    override val name = "Urgency Module"
    override var active = true

    override fun process(email: Email): WarningLog {

        val warningLog = WarningLog(Warning.HIGH_IMPORTANCE)

        if (email.importance == "high") warningLog.incrementOccurrences(Warning.HIGH_IMPORTANCE)
        return warningLog
    }
}