package phishingnet.contentAnalysis.modules.mock_modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.toOccurrences

class HardCodedWordCounter: AnalysisModule {
    override val name: String = "Hard Coded Word Counter"
    override var active: Boolean = true

    override fun process(email: Email): WarningLog {
        val warning = Warning.WORD_COUNTED
        val warningLog = WarningLog(warning)

        warningLog.warnings[warning] = 3.toOccurrences()

        return warningLog
    }
}