package com.example.demo.contentAnalysis.modules.mock_modules

import com.example.demo.contentAnalysis.models.AnalysisModule
import com.example.demo.contentAnalysis.models.Email
import com.example.demo.contentAnalysis.models.warnings.WarningLog
import com.example.demo.contentAnalysis.models.warnings.Warnings

class MyNameWasMentionedModule: AnalysisModule {
    override val name = "My name was mentioned somewhere"
    override var active = true

    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(Warnings.NAME_MENTIONED)
        val myName = "Manuel"

        val words = email.body.split("\\W+".toRegex())
        val counter = words.count { it.equals(myName, ignoreCase = true) }

        warningLog.setOccurrences(counter)
        return warningLog
    }
}