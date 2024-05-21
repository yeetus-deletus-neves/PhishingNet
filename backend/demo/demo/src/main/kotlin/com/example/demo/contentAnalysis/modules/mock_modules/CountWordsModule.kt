package com.example.demo.contentAnalysis.modules.mock_modules


import com.example.demo.contentAnalysis.models.AnalysisModule
import com.example.demo.contentAnalysis.models.*
import com.example.demo.contentAnalysis.models.warnings.WarningLog
import com.example.demo.contentAnalysis.models.warnings.Warnings


class CountWordsModule : AnalysisModule {
    override val name: String = "Count Words Module"
    override var active: Boolean = false
    override fun process(email: Email): WarningLog? {
        val warning = WarningLog(Warnings.WORD_COUNTED)

        val punctuation = setOf('.', ',', '!', '?', ';', ':', '"', '\'')
        val cleanStr = email.cleanContent.filterNot { it in punctuation }

        val counter = cleanStr.split(" ").filter { it.isNotBlank() }.size
        warning.setOccurrences(counter)

        return if (warning.occurrences() != 0) warning else null
    }

}