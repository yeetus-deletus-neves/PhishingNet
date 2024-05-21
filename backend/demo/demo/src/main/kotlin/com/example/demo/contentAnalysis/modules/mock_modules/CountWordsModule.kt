package com.example.demo.contentAnalysis.modules.mock_modules


import com.example.demo.contentAnalysis.models.AnalysisModule
import com.example.demo.contentAnalysis.models.*
import com.example.demo.contentAnalysis.models.warnings.WarningLog
import com.example.demo.contentAnalysis.models.warnings.Warnings


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