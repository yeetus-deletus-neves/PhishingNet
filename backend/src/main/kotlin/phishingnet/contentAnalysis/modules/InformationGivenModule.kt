package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog

class InformationGivenModule: AnalysisModule {
    override val name: String = "Information Asked Module"
    override var active: Boolean = false

    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(Warning.ASKS_FOR_SENSITIVE_INFO)

        val phoneNumberPattern = "\\+?[0-9]{1,3}[-.\\s]?\\(?[0-9]{1,4}?\\)?[-.\\s]?[0-9]{1,4}[-.\\s]?[0-9]{1,9}".toRegex()
        val ibanPattern = "[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}".toRegex()

        val phoneNumbersCount = phoneNumberPattern.findAll(email.body).count()
        val ibanCount = ibanPattern.findAll(email.body).count()


        warningLog[Warning.ASKS_FOR_SENSITIVE_INFO] = phoneNumbersCount + ibanCount

        return warningLog
    }
}