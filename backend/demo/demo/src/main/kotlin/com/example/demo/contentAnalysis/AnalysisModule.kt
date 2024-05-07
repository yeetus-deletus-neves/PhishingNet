package com.example.demo.contentAnalysis

import com.example.demo.contentAnalysis.models.Email
import com.example.demo.contentAnalysis.models.RiskAnalysis

interface AnalysisModule {
    val name: String
    var active: Boolean

    //should it be RiskAnalysis here? or a map/set that corresponds a threat to each risk?
    fun process(email: Email): RiskAnalysis
}