package phishingnet.contentAnalysis.modules.mock_modules


import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.toOccurrences


class CountWordsModule : AnalysisModule {
    override val name: String = "Count Words Module"
    override var active: Boolean = false
    override fun process(email: Email): WarningLog {
        val warning = Warning.WORD_COUNTED
        val warningLog = WarningLog(warning)

        val punctuation = setOf('.', ',', '!', '?', ';', ':', '"', '\'')

        val cleanStr = email.body.filterNot { it in punctuation }

        val occurrences = cleanStr.split(" ").filter { it.isNotBlank() }.size.toOccurrences()
        warningLog.warnings[warning] = occurrences

        return warningLog
    }

}