package phishingnet.api.http.controllers

import mu.KotlinLogging
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
    private val logger = KotlinLogging.logger {}

    @PostMapping(Uris.Analysis.ANALYSE)
    fun analyseContent(user: User, @RequestBody messageID: MessageRequest): ResponseEntity<*> {
        logger.info { "POST: ${Uris.Analysis.ANALYSE} recebido" }

        val id = messageID.messageID
        return when(val res = analysisServices.analyseMessage(user, id)) {
            is AnalysisResult.CompletedAnalysis -> ResponseTemplate.Ok(res.result, "Análise completa para o $id.")
            is AnalysisResult.WasInCache -> ResponseTemplate.Ok(res.result,"Análise completa para o $id.")
            is AnalysisResult.NoMessageToBeAnalyzed -> ResponseTemplate.NoContent("Nenhuma mensagem a ser analisada.", "Nenhuma mensagem a ser analisada.")
            is AnalysisResult.AccountNotLinked ->  ResponseTemplate.NotFound("A conta não está ligada a uma conta Microsoft.")
            is AnalysisResult.BadRequest -> ResponseTemplate.BadRequest("Comunicação inválida com os servidores da Microsoft.\n")
            is AnalysisResult.InvalidToken -> ResponseTemplate.Unauthorized("Acesso não autorizado aos servidores Microsoft. \n")
            is AnalysisResult.MessageNotFound -> ResponseTemplate.NotFound("A mensagem não foi encontrada. Certifique-se de que o messageID está correto.")
            is AnalysisResult.UnexpectedError -> ResponseTemplate.InternalServerError("Ocorreu um erro inesperado durante a análise $id. \n")
        }
    }
}