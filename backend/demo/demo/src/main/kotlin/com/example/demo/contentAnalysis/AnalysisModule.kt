package com.example.demo.contentAnalysis

import com.example.demo.contentAnalysis.models.Email
import com.example.demo.contentAnalysis.models.RiskAnalysis

interface AnalysisModule {
    val name: String
    var active: Boolean

    //TO ANALYSE CONTENT WE SHOULD USE THE CLEANED CONTENT NOT THE BODY
    fun process(email: Email): RiskAnalysis
}