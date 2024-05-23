package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.warnings.Warnings

class HeaderModule() : AnalysisModule {
    override val name: String = "Header Module"
    override var active: Boolean = false

    //TODO change implementation
    // we can't assume that just because these two: mailHeaderInfo.from, emailHeaderInfo.returnPath differ
    // that it is illegitimate

    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(Warnings.FROM_DISTINCT_FROM_RETURN_PATH)

        val emailHeaderInfo = email.internetHeaders
        val emailFrom = emailHeaderInfo.from.split('<','>')[1]
        if (emailFrom != emailHeaderInfo.returnPath) warningLog.incrementOccurrences()

        return warningLog
    }
}