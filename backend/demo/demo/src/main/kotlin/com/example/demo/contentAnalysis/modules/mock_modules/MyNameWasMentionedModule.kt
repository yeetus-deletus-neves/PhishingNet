package com.example.demo.contentAnalysis.modules.mock_modules

import com.example.demo.contentAnalysis.models.AnalysisModule
import com.example.demo.contentAnalysis.models.Email
import com.example.demo.contentAnalysis.models.warnings.WarningLog
import com.example.demo.contentAnalysis.models.warnings.Warnings

class MyNameWasMentionedModule: AnalysisModule {
    override val name = "My name was mentioned somewhere"
    override var active = true

    override fun process(email: Email): WarningLog? {
        val warning = WarningLog(Warnings.NAME_MENTIONED)
        val myName = "Manuel"

        val palavras = email.cleanContent.split("\\W+".toRegex())
        val counter = palavras.count { it.equals(myName, ignoreCase = true) }

        warning.setOccurrences(counter)
        return if (warning.occurrences() != 0) warning else null
    }
}