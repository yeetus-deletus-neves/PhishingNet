package com.example.demo.data.repositories

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
}