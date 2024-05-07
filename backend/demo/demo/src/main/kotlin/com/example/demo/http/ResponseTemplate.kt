package com.example.demo.http

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ApiError(
    val status: Int,
    val message: String,
    val details: String?
)

object ResponseTemplate {

    private val logger = KotlinLogging.logger {}

    fun Unauthorized(details: String): ResponseEntity<*> {
        val code = HttpStatus.UNAUTHORIZED.value()
        logger.warn { "Unauthorized $code: $details" }

        val error = ApiError(code, "Unauthorized", details)
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    fun BadRequest(details: String): ResponseEntity<*> {
        val code = HttpStatus.BAD_REQUEST.value()
        logger.warn { "Bad request $code: $details" }

        val error = ApiError(code, "Bad Request", details)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    fun Conflict(details: String): ResponseEntity<*> {
        val code = HttpStatus.CONFLICT.value()
        logger.warn { "Conflict: $code: $details" }

        val error = ApiError(code, "Conflict", details)
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

    fun NotFound(details: String): ResponseEntity<*> {
        val code = HttpStatus.NOT_FOUND.value()
        logger.warn { "Not found: $code: $details" }

        val error = ApiError(code, "Not Found", details)
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    fun Ok(body: Any?, log: String?): ResponseEntity<*> {
        val code = HttpStatus.OK.value()
        logger.warn { "Ok: $code: $log" }

        return ResponseEntity(body, HttpStatus.OK)
    }

    fun Created(body: Any?, log: String?): ResponseEntity<*> {
        val code = HttpStatus.CREATED.value()
        logger.warn { "Created: $code: $log" }

        return ResponseEntity(body, HttpStatus.CREATED)
    }

    fun InternalServerError(details: String): ResponseEntity<*> {
        val code = HttpStatus.INTERNAL_SERVER_ERROR.value()
        logger.warn { "Internal Server Error: $code: $details" }

        val error = ApiError(code, "Internal Server Error", details)
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}