package com.example.demo.services.analysisServices


interface AnalysisModule {

    fun process(content: String): RiskAnalysis
}