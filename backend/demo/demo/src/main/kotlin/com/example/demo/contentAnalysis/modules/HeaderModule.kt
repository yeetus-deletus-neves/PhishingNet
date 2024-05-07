package com.example.demo.contentAnalysis.modules

import com.example.demo.contentAnalysis.AnalysisModule
import com.example.demo.contentAnalysis.models.Email
import com.example.demo.contentAnalysis.models.RiskAnalysis

class HeaderModule() : AnalysisModule {
    override val name: String = "Header Module"
    override var active: Boolean = false
    override fun process(email: Email): RiskAnalysis {
        TODO("Not yet implemented")
    }
}