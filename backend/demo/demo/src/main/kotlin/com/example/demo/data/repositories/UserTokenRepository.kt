package com.example.demo.data.repositories

import com.example.demo.data.entities.UserToken
import java.util.*

interface UserTokenRepository {

    fun createUserToken(userID: UUID, token: String): UserToken?
    fun getUserToken(token: String): UserToken?
    fun deleteUserToken(token: String): UserToken?
}