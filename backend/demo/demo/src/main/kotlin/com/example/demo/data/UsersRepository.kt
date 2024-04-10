package com.example.demo.data

import com.example.demo.data.entities.RefreshToken
import com.example.demo.data.entities.User
import com.example.demo.data.entities.UserToken
import java.util.UUID

interface UsersRepository {
    fun createUser(username: String, password: String): User?
    fun deleteUser(userID: UUID): UUID?
    fun getUserById(userID: UUID): User?
    fun getUserByUsername(username: String): User?
    fun getUserByToken(token: String): User?

    fun createUserToken(userID: UUID, token: String): UserToken?
    fun deleteUserToken(token: String): UserToken?
    fun getUserToken(token: String): UserToken?

    fun updateRefreshToken(userID: UUID, newToken: String): RefreshToken?
    fun getRefreshToken(userID: UUID): RefreshToken?
}