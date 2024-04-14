package com.example.demo.data.repositories

import com.example.demo.data.entities.UserToken
import java.time.Instant
import java.util.*

interface UserTokenRepository {

    fun createUserToken(userID: UUID, token: String, instant: Instant): UserToken?
    fun getUserToken(token: String): UserToken?
    fun updateUserTokenLastUsed(token: String, lastUsed: Instant): UserToken?
    fun deleteUserToken(token: String): UserToken?
}