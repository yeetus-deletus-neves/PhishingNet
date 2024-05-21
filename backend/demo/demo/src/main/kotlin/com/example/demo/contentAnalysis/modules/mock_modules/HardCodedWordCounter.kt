package com.example.demo.contentAnalysis.modules.mock_modules

import com.example.demo.contentAnalysis.models.AnalysisModule
import com.example.demo.contentAnalysis.models.Email
import com.example.demo.contentAnalysis.models.warnings.WarningLog
import com.example.demo.contentAnalysis.models.warnings.Warnings

class HardCodedWordCounter: AnalysisModule{
    override val name: String = "Hard Coded Word Counter"
    override var active: Boolean = true

    override fun process(email: Email): WarningLog {
        val warning = WarningLog(Warnings.WORD_COUNTED)
        warning.setOccurrences(3)

        return warning
    }
}