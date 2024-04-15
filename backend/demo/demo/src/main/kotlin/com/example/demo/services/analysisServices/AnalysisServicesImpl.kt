package com.example.demo.services.analysisServices

import com.example.demo.data.entities.User
import org.springframework.stereotype.Service

@Service
class AnalysisServicesImpl: AnalysisServices {
    override fun proccess(user: User, content: String): AnalysisResult {
        //TODO("Not yet implemented")
        return AnalysisResult.CompletedAnalysis("TODO")
    }
}
sealed class AnalysisResult{
    data class CompletedAnalysis(val result: String): AnalysisResult()
    object AccountNotLinked: AnalysisResult()
}