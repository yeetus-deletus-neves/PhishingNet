package com.example.demo.contentAnalysis.models

import com.example.demo.contentAnalysis.AnalysisModule

data class ModuleRegistry (
    val name: String,
    private val moduleInitializer: () -> AnalysisModule,
    var active: Boolean = false

)