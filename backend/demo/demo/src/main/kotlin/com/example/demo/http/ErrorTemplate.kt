package com.example.demo.http

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ApiError(
    val status: Int,
    val message: String,
    val details: String?
)

object ErrorTemplate {

    fun Unauthorized(details: String): ResponseEntity<*> {
        val error = ApiError(401, "Unauthorized", details)
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    fun BadRequest(details: String): ResponseEntity<*> {
        val error = ApiError(400, "Bad Request", details)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    fun Conflict(details: String): ResponseEntity<*> {
        val error = ApiError(409, "Conflict", details)
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

    fun NotFound(details: String): ResponseEntity<*> {
        val error = ApiError(404, "Not Found", details)
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

}