package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.SecurityVerification
import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.warnings.Warning

class HeaderModule : AnalysisModule {
    override val name: String = "Header Module"
    override var active: Boolean = false

    //TODO change implementation
    // we can't assume that just because these two: mailHeaderInfo.from, emailHeaderInfo.returnPath differ
    // that it is illegitimate

    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(
            listOf(
                Warning.FAILED_HEADERS_AND_RETURN_PATH_CHECK,
            )
        )


        if (email.authDetails.dmarc == SecurityVerification.FAILED ||
            email.authDetails.dkim == SecurityVerification.FAILED ||
            email.authDetails.spf == SecurityVerification.FAILED
        ) warningLog.incrementOccurrences(Warning.FAILED_HEADERS_AND_RETURN_PATH_CHECK)

        val emailFrom = email.from.address//.split('<','>')[1]
        val returnPath = email.returnPath
        if (emailFrom != returnPath) warningLog.incrementOccurrences(Warning.FAILED_HEADERS_AND_RETURN_PATH_CHECK)

        return warningLog
    }
}