package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog

class FromHistoryModule: AnalysisModule {
    override val name = "From History Module"
    override var active = false

    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(Warning.PAST_EMAILS_SENT)
        warningLog[Warning.PAST_EMAILS_SENT] = email.fromEmailCount

        return warningLog
    }
}