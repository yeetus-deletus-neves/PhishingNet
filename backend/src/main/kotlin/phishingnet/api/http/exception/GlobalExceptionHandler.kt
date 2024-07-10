package phishingnet.api.http.exception

import phishingnet.api.http.ResponseTemplate
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleException(e: Exception): ResponseEntity<*> {
        logger.error(e) { "Ocorreu um erro ao processar o pedido. \n Erro: ${e.localizedMessage}" }

        return ResponseTemplate.InternalServerError(
            "Erro interno do servidor: algo correu mal! " +
                    "Por favor tente novamente mais tarde ou contacte os administradores do servidor."
        )
    }

}