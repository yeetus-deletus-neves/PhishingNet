package com.example.demo.http.exception

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
    fun handleException(e: Exception): ResponseEntity<String> {
        logger.error(e) { "An error occurred while handling request. \n Error: ${e.localizedMessage}" }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Internal Server Error: Something went wrong! Please try again later or contact the server administrators.")
    }

}