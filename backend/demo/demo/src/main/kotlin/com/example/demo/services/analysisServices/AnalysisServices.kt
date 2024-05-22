package com.example.demo.services.analysisServices;

import com.example.demo.data.entities.User

interface AnalysisServices {
    fun analyseMessage(user: User, messageID: String): AnalysisResult
}
