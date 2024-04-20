package com.example.demo.services.userServices

import com.example.demo.data.entities.User
import com.example.demo.data.entities.UserToken
import java.util.*

interface UserServices {

    fun createUser(username: String, password: String): CreateUserInfo
    fun getUserById(userID: String): GetUserInfo
    fun getUserByToken(token: String): GetUserInfo

    fun createUserToken(username: String, password: String): CreateUserTokenInfo
    fun updateUserTokenLastUsed(token: String): UserToken?

    fun createRefreshToken(user: User, token: String): CreateRefreshTokenInfo
    fun removeRefreshToken(user: User): RemoveRefreshTokenInfo
    fun updateRefreshToken(user: User, newToken: String): UpdateRefreshTokenInfo
    fun getRefreshToken(user: User): GetRefreshTokenInfo
}