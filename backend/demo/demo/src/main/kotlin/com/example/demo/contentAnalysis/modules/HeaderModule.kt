package com.example.demo.contentAnalysis.modules

import com.example.demo.contentAnalysis.models.AnalysisModule
import com.example.demo.contentAnalysis.models.*
import com.example.demo.contentAnalysis.models.riskAnalysis.RiskAnalysis
import com.example.demo.contentAnalysis.models.warnings.WarningLog

class HeaderModule() : AnalysisModule {
    override val name: String = "Header Module"
    override var active: Boolean = false

    //TODO change implementation
    // we can't assume that just because these two: mailHeaderInfo.from, emailHeaderInfo.returnPath differ
    // that it is illegitimate

    override fun process(email: Email): RiskAnalysis {
        val emailHeaderInfo = email.msgHeadersInfo
        val emailFrom = emailHeaderInfo.from.split('<','>')[1]
        return evaluate(emailFrom != emailHeaderInfo.returnPath)
    }

    private fun evaluate(detectedInfraction: Boolean): RiskAnalysis {
        return if (detectedInfraction) mapOf(Risk.FALSE_ENTITY to RiskAnalysisEntry(ThreatLevel.Suspicious, name))
        else mapOf(Risk.FALSE_ENTITY to RiskAnalysisEntry(ThreatLevel.NoThreat, name))
    }
}