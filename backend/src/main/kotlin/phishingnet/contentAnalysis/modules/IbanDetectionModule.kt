package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog

class IbanDetectionModule: AnalysisModule {
    override val name: String = "Módulo de Deteção Iban"

    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(Warning.ASKS_FOR_IBAN)

        val ibanPattern = "[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}".toRegex()

        val ibanCount = ibanPattern.findAll(email.body).count()

        warningLog[Warning.ASKS_FOR_IBAN] = ibanCount

        return warningLog
    }
}