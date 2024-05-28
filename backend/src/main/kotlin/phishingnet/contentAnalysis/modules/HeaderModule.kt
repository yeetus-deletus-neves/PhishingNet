package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.SecurityVerification
import phishingnet.contentAnalysis.models.warnings.Occurrences
import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.warnings.Warning

class HeaderModule() : AnalysisModule {
    override val name: String = "Header Module"
    override var active: Boolean = false

    //TODO change implementation
    // we can't assume that just because these two: mailHeaderInfo.from, emailHeaderInfo.returnPath differ
    // that it is illegitimate

    override fun process(email: Email): WarningLog {
        val warningLog: WarningLog = WarningLog(
            listOf(
                Warning.FROM_DISTINCT_FROM_RETURN_PATH,
                Warning.DMARC_AUTH_FAILED,
                Warning.DKIM_AUTH_FAILED,
                Warning.SPF_AUTH_FAILED,
            )
        )


        if (email.authDetails.dmarc == SecurityVerification.FAILED)
            warningLog[Warning.DMARC_AUTH_FAILED].incrementOccurrences()
        if (email.authDetails.dkim == SecurityVerification.FAILED)
            warningLog[Warning.DKIM_AUTH_FAILED].incrementOccurrences()
        if (email.authDetails.spf == SecurityVerification.FAILED)
            warningLog[Warning.SPF_AUTH_FAILED].incrementOccurrences()

        val emailFrom = email.from.address//.split('<','>')[1]
        val returnPath = email.returnPath
        if (emailFrom != returnPath) warningLog[Warning.FROM_DISTINCT_FROM_RETURN_PATH].incrementOccurrences()

        return warningLog
    }
}