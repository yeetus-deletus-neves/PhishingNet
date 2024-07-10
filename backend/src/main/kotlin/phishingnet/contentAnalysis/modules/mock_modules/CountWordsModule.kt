package phishingnet.contentAnalysis.modules.mock_modules


import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.warnings.Warning


class CountWordsModule : AnalysisModule {
    override val name: String = "Módulo de contagem de palavras"
    override fun process(email: Email): WarningLog {
        val warning = Warning.WORD_COUNTED
        val warningLog = WarningLog(warning)

        val punctuation = setOf('.', ',', '!', '?', ';', ':', '"', '\'')

        val cleanStr = email.body.filterNot { it in punctuation }

        val occurrences = cleanStr.split(" ").filter { it.isNotBlank() }.size
        warningLog[warning] = occurrences

        return warningLog
    }

}