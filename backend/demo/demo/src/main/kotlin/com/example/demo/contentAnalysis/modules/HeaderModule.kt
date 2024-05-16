package com.example.demo.contentAnalysis.modules

import com.example.demo.contentAnalysis.models.AnalysisModule
import com.example.demo.contentAnalysis.models.*
import com.example.demo.contentAnalysis.models.riskAnalysis.RiskAnalysis
import com.example.demo.contentAnalysis.models.warnings.WarningLog

class HeaderModule() : AnalysisModule {
    override val name: String = "Header Module"
    override var active: Boolean = false
    override fun process(email: Email): WarningLog? {
        TODO("Not yet implemented")
    }

    private fun evaluate(detectedInfraction: Boolean): RiskAnalysis {
        TODO()
    }
}