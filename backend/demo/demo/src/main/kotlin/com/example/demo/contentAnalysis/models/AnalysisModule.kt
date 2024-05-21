package com.example.demo.contentAnalysis.models

import com.example.demo.contentAnalysis.models.warnings.WarningLog

interface AnalysisModule {
    val name: String
    var active: Boolean

    //TO ANALYSE CONTENT WE SHOULD USE THE CLEANED CONTENT NOT THE BODY
    fun process(email: Email): WarningLog
}