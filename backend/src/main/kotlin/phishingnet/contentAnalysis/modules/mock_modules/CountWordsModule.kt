package phishingnet.contentAnalysis.modules.mock_modules


import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.warnings.Warnings


class CountWordsModule : AnalysisModule {
    override val name: String = "Count Words Module"
    override var active: Boolean = false
    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(Warnings.WORD_COUNTED)

        val punctuation = setOf('.', ',', '!', '?', ';', ':', '"', '\'')

        val cleanStr = email.body.filterNot { it in punctuation }

        val counter = cleanStr.split(" ").filter { it.isNotBlank() }.size
        warningLog.setOccurrences(counter)

        return warningLog
    }

}