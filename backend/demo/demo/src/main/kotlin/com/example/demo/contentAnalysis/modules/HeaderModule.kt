package com.example.demo.contentAnalysis.modules

import com.example.demo.contentAnalysis.models.AnalysisModule
import com.example.demo.contentAnalysis.models.*
import com.example.demo.contentAnalysis.models.riskAnalysis.RiskAnalysis
import com.example.demo.contentAnalysis.models.warnings.WarningLog
import com.example.demo.contentAnalysis.models.warnings.Warnings

class HeaderModule() : AnalysisModule {
    override val name: String = "Header Module"
    override var active: Boolean = false

    //TODO change implementation
    // we can't assume that just because these two: mailHeaderInfo.from, emailHeaderInfo.returnPath differ
    // that it is illegitimate

    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(Warnings.FROM_DISTINCT_FROM_RETURN_PATH)

        val emailHeaderInfo = email.msgHeadersInfo
        val emailFrom = emailHeaderInfo.from.split('<','>')[1]
        if (emailFrom != emailHeaderInfo.returnPath) warningLog.incrementOccurrences()

        return warningLog
    }
}