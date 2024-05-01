package com.example.demo.contentAnalysis

import com.example.demo.contentAnalysis.models.Risk

interface AnalysisModule {

    fun process(content: String): List<Risk>
}