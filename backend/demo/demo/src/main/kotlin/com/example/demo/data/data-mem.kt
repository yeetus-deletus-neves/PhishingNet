package com.example.demo.data

import com.example.demo.data.entities.RefreshToken
import com.example.demo.data.entities.User
import com.example.demo.data.entities.UserToken
import com.example.demo.data.repositories.RefreshTokenRepository
import com.example.demo.data.repositories.UserTokenRepository
import com.example.demo.data.repositories.UsersRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class `data-mem` : UsersRepository, RefreshTokenRepository, UserTokenRepository {

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

    override fun getUserToken(token: String): UserToken? {
        val target: UserToken? = userTokenMem.find { it.userToken == token }

        if (target == null) return null
        return target
    }

    override fun updateUserTokenLastUsed(token: String, lastUsed: Instant): UserToken? {
        val target = getUserToken(token)
        if (target == null || target.lastUsed.isAfter(lastUsed) ) return null
        val newToken = UserToken(target.userID, target.userToken, lastUsed)

        userTokenMem.remove(target)
        userTokenMem.add(newToken)
        return newToken
    }

    override fun createUserToken(userID: UUID, token: String, lastUsed: Instant): UserToken? {
        val newToken = UserToken(
            userID,
            token,
            lastUsed
        )
        userTokenMem.add(newToken)
        return newToken
    }

    override fun deleteUserToken(token: String): UserToken? {
        val target: UserToken? = userTokenMem.find { it.userToken == token }

        if (target == null) return null
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