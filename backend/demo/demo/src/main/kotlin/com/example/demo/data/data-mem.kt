package com.example.demo.data

import com.example.demo.data.entities.RefreshToken
import com.example.demo.data.entities.User
import com.example.demo.data.entities.UserToken
import java.util.*
import kotlin.collections.ArrayList

class `data-mem` : UsersRepository {

    private val userMem: LinkedList<User> = LinkedList()
    private val userTokenMem: LinkedList<UserToken> = LinkedList()
    private val refreshTokenMem: LinkedList<RefreshToken> = LinkedList()

    override fun createUser(username: String, password: String): User? {
        val newID = UUID.randomUUID()
        val newUser = User(
            UUID.randomUUID(),
            username,
            password)
        val newRefreshToken = RefreshToken(newID, null)

        userMem.add(newUser)
        refreshTokenMem.push(newRefreshToken)
        return newUser
    }

    override fun getUserById(userID: UUID): User? {
        return userMem.find { it.userID == userID }
    }

    override fun getUserByUsername(username: String): User? {
        return userMem.find { it.username == username }
    }

    override fun deleteUser(userID: UUID): UUID? {
        val target: User = getUserById(userID) ?: return null

        userMem.remove(target)
        return target.userID
    }

    override fun getUserByToken(token: String): User? {
        val target: UserToken? = userTokenMem.find { it.userToken == token }

        if (target == null) return null
        return getUserById(target.userID)
    }

    override fun createUserToken(userID: UUID, token: String): UserToken? {
        val newToken = UserToken(
            userID,
            token
        )
        userTokenMem.add(newToken)
        return newToken
    }

    override fun getUserToken(token: String): UserToken? {
        return userTokenMem.find { it.userToken == token }
    }

    override fun deleteUserToken(token: String): UserToken? {
        val target: UserToken = getUserToken(token) ?: return null

        userTokenMem.remove(target)
        return target
    }

    override fun getRefreshToken(userID: UUID): RefreshToken? {
        return refreshTokenMem.find { it.userID == userID }
    }

    override fun updateRefreshToken(userID: UUID, newToken: String): RefreshToken? {
        val target: RefreshToken = getRefreshToken(userID) ?: return null

        target.refreshToken = newToken
        return target
    }


}