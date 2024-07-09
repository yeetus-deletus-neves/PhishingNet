package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog


/***
 * O retorno deste Módulo é um inteiro com significado correspondente a um booleano
 */
class ReturnPathModule : AnalysisModule {
    override val name: String = "Return Path Module"
    override var active: Boolean = false

    //TODO change implementation
    // we can't assume that just because these two: mailHeaderInfo.from, emailHeaderInfo.returnPath differ
    // that it is illegitimate

    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(listOf(Warning.FROM_DISTINCT_FROM_RETURN_PATH))

        val emailFrom = email.from.address//.split('<','>')[1]
        val returnPath = email.returnPath
        if (emailFrom != returnPath) warningLog.incrementOccurrences(Warning.FROM_DISTINCT_FROM_RETURN_PATH)

        return warningLog
    }
}