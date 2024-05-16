package com.example.demo.contentAnalysis.modules

import com.example.demo.contentAnalysis.AnalysisModule
import com.example.demo.contentAnalysis.models.*

class HeaderModule() : AnalysisModule {
    override val name: String = "Header Module"
    override var active: Boolean = false

    //TODO change implementation
    // we can't assume that just because these two: mailHeaderInfo.from, emailHeaderInfo.returnPath differ
    // that it is illegitimate


    override fun process(email: Email): RiskAnalysis {
        val emailHeaderInfo = email.msgHeadersInfo
        return evaluate(emailHeaderInfo.from != emailHeaderInfo.returnPath)
    }

    private fun evaluate(detectedInfraction: Boolean): RiskAnalysis {
        return if (detectedInfraction) {
            mapOf(Risk.FALSE_ENTITY to RiskAnalysisEntry(ThreatLevel.Suspicious, name))
        } else {
            mapOf(Risk.FALSE_ENTITY to RiskAnalysisEntry(ThreatLevel.NoThreat, name))
        }
    }
}