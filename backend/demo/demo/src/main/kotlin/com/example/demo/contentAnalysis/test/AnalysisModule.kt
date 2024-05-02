package com.example.demo.contentAnalysis.test


interface AnalysisModule {

    fun process(content: String): RiskAnalysis
}