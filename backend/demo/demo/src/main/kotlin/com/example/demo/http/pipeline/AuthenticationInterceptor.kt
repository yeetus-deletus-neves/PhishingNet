package com.example.demo.http.pipeline

import com.example.demo.data.entities.User
import com.example.demo.http.ResponseTemplate
import com.google.gson.Gson
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthenticationInterceptor(
    private val authorizationHeaderProcessor: AuthorizationHeaderProcessor
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod && handler.methodParameters.any { it.parameterType == User::class.java }
        ) {
            val user = authorizationHeaderProcessor.process(request.getHeader(NAME_AUTHORIZATION_HEADER))
            return if (user == null) {
                response.status = 401
                response.writer.write(Gson().toJson(ResponseTemplate.Unauthorized("Authentication failed").body))
                response.addHeader(NAME_WWW_AUTHENTICATE_HEADER, AuthorizationHeaderProcessor.SCHEME)
                response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                false
            } else {
                UserArgumentResolver.addUserTo(user, request)
                true
            }
        }

        return true
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthenticationInterceptor::class.java)
        private const val NAME_AUTHORIZATION_HEADER = "Authorization"
        private const val NAME_WWW_AUTHENTICATE_HEADER = "WWW-Authenticate"
    }
}