package com.example.demo.services.analysisServices

import org.springframework.stereotype.Service

@Service
class AnalysisServicesImpl: AnalysisServices {

    override fun process(content: String): AnalysisResult {
        return AnalysisResult.CompletedAnalysis("TODO")
    }
}
sealed class AnalysisResult{
    data class CompletedAnalysis(val result: String): AnalysisResult()
    object AccountNotLinked: AnalysisResult()
}