package phishingnet.api.http.controllers

import phishingnet.api.data.entities.User
import phishingnet.api.http.ResponseTemplate
import phishingnet.api.http.Uris
import phishingnet.api.services.analysisServices.AnalysisResult
import phishingnet.api.services.analysisServices.AnalysisServices
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import phishingnet.api.http.models.MessageRequest

@RestController
class AnalysisController(
    private val analysisServices: AnalysisServices
) {

    @PostMapping(Uris.Analysis.ANALYSE)
    fun analyseContent(user: User, @RequestBody messageID: MessageRequest): ResponseEntity<*> {
        val id = messageID.messageID
        return when(val res = analysisServices.analyseMessage(user, id)) {
            is AnalysisResult.CompletedAnalysis -> ResponseTemplate.Ok(res, "Completed analysis of $id")
            is AnalysisResult.AccountNotLinked ->  ResponseTemplate.NotFound("The account is not linked to a Microsoft account.")
            is AnalysisResult.BadRequest -> ResponseTemplate.BadRequest("Invalid while communicating with the Microsoft Servers. \n" + res.log)
            is AnalysisResult.InvalidToken -> ResponseTemplate.Unauthorized("Unauthorized access to the Microsoft Servers \n" + res.log)
            is AnalysisResult.MessageNotFound -> ResponseTemplate.NotFound("Message was not found. Please make sure the messageID is correct.")
            is AnalysisResult.UnexpectedError -> ResponseTemplate.InternalServerError("Unexpected error occurred while analysing $id \n" + res.log)
        }
    }
}