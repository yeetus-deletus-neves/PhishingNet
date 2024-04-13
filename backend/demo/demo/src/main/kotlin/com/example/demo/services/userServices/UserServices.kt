package com.example.demo.services.userServices

import com.example.demo.data.entities.RefreshToken
import com.example.demo.data.entities.User
import com.example.demo.data.entities.UserToken
import java.util.*

interface UserServices {

    fun createUser(username: String, password: String): CreateUserInfo
    fun getUserById(userID: UUID): GetUserInfo
    fun getUserByToken(token: String): GetUserInfo

    fun createUserToken(username: String, password: String): CreateUserTokenInfo
    fun validateUserToken(token: String): ValidateUserTokenInfo

    fun updateRefreshToken(userToken: String, newToken: String): UpdateRefreshTokenInfo
    fun getRefreshToken(userToken: String): GetRefreshTokenInfo
}