package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog


/***
 * O retorno deste Módulo é um inteiro com significado correspondente a um booleano
 * não podemos assumir que só porque emailHeaderInfo.from e emailHeaderInfo.returnPath diferem que é ilegítimo
 */
class ReturnPathModule : AnalysisModule {
    override val name: String = "Return Path Module"

    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(listOf(Warning.FROM_DISTINCT_FROM_RETURN_PATH))

        val emailFrom = email.from.address//.split('<','>')[1]
        val returnPath = email.returnPath
        if (emailFrom != returnPath) warningLog.incrementOccurrences(Warning.FROM_DISTINCT_FROM_RETURN_PATH)

        return warningLog
    }
}