package com.example.demo.http.controllers

import com.example.demo.data.entities.User
import com.example.demo.http.ResponseTemplate
import com.example.demo.http.Uris
import com.example.demo.http.models.ContentInputModel
import com.example.demo.services.analysisServices.AnalysisResult
import com.example.demo.services.analysisServices.AnalysisServices
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AnalysisController(
    private val analysisServices: AnalysisServices
) {

    @PostMapping(Uris.Analysis.ANALYSE)
    fun analyseContent(user: User, @RequestBody content: ContentInputModel): ResponseEntity<*> {
        return when(val res = analysisServices.analyseMessage(user, content.content)) {
            is AnalysisResult.AccountNotLinked ->  ResponseTemplate.BadRequest("Testing")
            is AnalysisResult.CompletedAnalysis -> ResponseTemplate.Ok(res, "Completed analysis of ${content.content}")
            is AnalysisResult.UnableToRetrieveRefresh -> ResponseTemplate.NotFound("An unexpected error occurred")
        }
    }
}