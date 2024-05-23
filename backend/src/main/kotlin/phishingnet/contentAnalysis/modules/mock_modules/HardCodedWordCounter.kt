package phishingnet.contentAnalysis.modules.mock_modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.warnings.Warnings

class HardCodedWordCounter: AnalysisModule {
    override val name: String = "Hard Coded Word Counter"
    override var active: Boolean = true

    override fun process(email: Email): WarningLog {
        val warning = WarningLog(Warnings.WORD_COUNTED)
        warning.setOccurrences(3)

        return warning
    }
}