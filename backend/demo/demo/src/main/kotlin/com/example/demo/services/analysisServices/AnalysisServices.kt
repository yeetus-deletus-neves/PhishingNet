package com.example.demo.services.analysisServices

import com.example.demo.data.entities.User

interface AnalysisServices {

    fun proccess(user: User, content: String): AnalysisResult
}