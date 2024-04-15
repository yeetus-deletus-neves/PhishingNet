package com.example.demo.http.pipeline

import com.example.demo.data.entities.User
import com.example.demo.services.userServices.GetUserInfo
import com.example.demo.services.userServices.UserServices
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AuthorizationHeaderProcessor(
    val usersService: UserServices
) {

    fun process(authorizationValue: String?): User? {
        if (authorizationValue == null) {
            return null
        }
        val parts = authorizationValue.trim().split(" ")
        if (parts.size != 2) {
            return null
        }
        if (parts[0].lowercase() != SCHEME) {
            return null
        }

        return when (val x = usersService.getUserByToken(parts[1])){
            is GetUserInfo.UserFound -> x.user
            else -> {
                null
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthorizationHeaderProcessor::class.java)
        const val SCHEME = "bearer"
    }
}