package com.example.demo.contentAnalysis.modules

import com.example.demo.contentAnalysis.AnalysisModule
import com.example.demo.contentAnalysis.models.*

class InformationAskedModule: AnalysisModule  {
    override val name: String = "Information Asked Module"
    override var active: Boolean = false

    //problem with different languages
    //implementation should be based on count of infractions or map key value?
    override fun process(email: Email): RiskAnalysis {
        val suspiciousInfo = detectSuspiciousInquiries(email.cleanContent)
        val analysis = evaluate(suspiciousInfo)

        return analysis
    }

    private fun evaluate(cnt: Int): RiskAnalysis {
        val analysis = mutableMapOf<Risk, RiskAnalysisEntry>()

        analysis[Risk.MOCK_RISK] = RiskAnalysisEntry(calcThreat(cnt), name)
        return analysis
    }

    private fun calcThreat(cnt: Int): ThreatLevel {
        return if (cnt <= 5) ThreatLevel.NoThreat
        else if (cnt <= 10) ThreatLevel.Suspicious
        else ThreatLevel.VerySuspicious
    }

    private fun detectSuspiciousInquiries(str: String): Int {
        TODO()
        /*
        if(str.contains("iban"))
        if(str.contains("cc"))
        */
    }

}