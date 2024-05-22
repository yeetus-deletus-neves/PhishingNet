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
    fun analyseContent(user: User, @RequestBody messageID: String): ResponseEntity<*> {
        return when(val res = analysisServices.analyseMessage(user, messageID)) {
            is AnalysisResult.CompletedAnalysis -> ResponseTemplate.Ok(res, "Completed analysis of $messageID")
            is AnalysisResult.AccountNotLinked ->  ResponseTemplate.NotFound("The account is not linked to a Microsoft account.")
            is AnalysisResult.BadRequest -> ResponseTemplate.BadRequest("Invalid while communicating with the Microsoft Servers. \n" + res.log)
            is AnalysisResult.InvalidToken -> ResponseTemplate.Unauthorized("Unauthorized access to the Microsoft Servers \n" + res.log)
            is AnalysisResult.MessageNotFound -> ResponseTemplate.NotFound("Message was not found. Please make sure the messageID is correct.")
            is AnalysisResult.UnexpectedError -> ResponseTemplate.InternalServerError("Unexpected error occurred while analysing $messageID \n" + res.log)
        }
    }
}