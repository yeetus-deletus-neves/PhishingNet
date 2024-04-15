package com.example.demo.services.userServices

import com.example.demo.data.entities.RefreshToken
import com.example.demo.data.entities.User
import com.example.demo.data.entities.UserToken
import com.example.demo.data.repositories.RefreshTokenRepository
import com.example.demo.data.repositories.UsersRepository
import com.example.demo.data.repositories.UserTokenRepository
import com.example.demo.utils.Clock
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.util.*

@Service
class UserServicesImpl(
    private val usersRepository: UsersRepository,
    private val userTokenRepository: UserTokenRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passEncoder: PasswordEncoder,
    private val clock: Clock
): UserServices {

    @Transactional
    override fun createUser(username: String, password: String): CreateUserInfo {
        if (!isPasswordSecure(password)) return CreateUserInfo.UnsafePassword
        if (usersRepository.findUserByUsername(username) != null) return CreateUserInfo.UserAlreadyExists

        val newUser = User(username, passEncoder.encode(password))
        usersRepository.save(newUser)
        return CreateUserInfo.UserCreated(newUser)
    }

    @Transactional(readOnly = true)
    override fun getUserById(userID: UUID): GetUserInfo {
        val user = usersRepository.findUserById(userID) ?: return  GetUserInfo.UserNotFound
        return GetUserInfo.UserFound(user)
    }

    @Transactional(readOnly = true)
    override fun getUserByToken(token: String): GetUserInfo {
        val myToken = userTokenRepository.findUserTokenByTokenvalidationinfo(token) ?: return  GetUserInfo.UserNotFound
        return GetUserInfo.UserFound(myToken.userid!!)
    }

    @Transactional
    override fun createUserToken(username: String, password: String): CreateUserTokenInfo {
        val user = usersRepository.findUserByUsername(username)
        if ( user == null || !passEncoder.matches(password, user.passwordinfo)) return CreateUserTokenInfo.AuthenticationFailed

        val newToken = getToken(user)
        return CreateUserTokenInfo.TokenCreated(newToken)
    }

    @Transactional
    override fun updateUserTokenLastUsed(token: String): UserToken? {
        val myToken = userTokenRepository.findUserTokenByTokenvalidationinfo(token) ?: return null
        myToken.lastUsedAt = clock.now().toEpochMilli()

        userTokenRepository.save(myToken)
        return myToken
    }

    @Transactional
    override fun createRefreshToken(user: User, token: String): CreateRefreshTokenInfo {
        val myToken = refreshTokenRepository.findRefreshTokensByUserid(user)
        if (myToken != null) return CreateRefreshTokenInfo.TokenAlreadyExists

        //TODO: Usar token recebido para is buscar access token
        val rToken = token

        val newToken = RefreshToken(user, rToken)
        refreshTokenRepository.save(newToken)
        return CreateRefreshTokenInfo.TokenCreated
    }

    @Transactional
    override fun removeRefreshToken(user: User): RemoveRefreshTokenInfo {
        val myToken = refreshTokenRepository.findRefreshTokensByUserid(user) ?: return RemoveRefreshTokenInfo.AccountNotLinked

        refreshTokenRepository.delete(myToken)
        return RemoveRefreshTokenInfo.AccountUnlinked
    }

    @Transactional
    override fun updateRefreshToken(user: User, newToken: String): UpdateRefreshTokenInfo {
        val token = refreshTokenRepository.findRefreshTokensByUserid(user) ?: return UpdateRefreshTokenInfo.UserNotFound
        token.rtoken = newToken
        refreshTokenRepository.save(token)

        return UpdateRefreshTokenInfo.TokenUpdated
    }

    @Transactional(readOnly = true)
    override fun getRefreshToken(user: User): GetRefreshTokenInfo {
        val token = refreshTokenRepository.findRefreshTokensByUserid(user) ?: return GetRefreshTokenInfo.TokenNotFound

        return GetRefreshTokenInfo.TokenFound(token)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  AUXILIARY FUNCTIONS                                       //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun isPasswordSecure(password: String): Boolean{
        var hasCapital = false
        var hasLower = false
        var hasNumber = false

        for(i in password.indices){
            if (password[i].isUpperCase()) {
                hasCapital = true
            }
            if (password[i].isLowerCase()) {
                hasLower = true
            }
            if (password[i].isDigit()) {
                hasNumber = true
            }
        }

        return hasLower && hasCapital && hasNumber && password.length >= 8
    }

    private fun getToken(user: User): UserToken {
        val tokenCount = userTokenRepository.countUserTokensByUserid(user)

        // If theres already the max number of tokens, deletes the last used
        if (tokenCount >= UserToken.MAX_TOKENS) {
            val toDelete = tokenCount - UserToken.MAX_TOKENS + 1
            for (n in 1..toDelete) {
                val invalidToken = userTokenRepository.findTopByUseridOrderByLastUsedAtAsc(user) ?: continue
                userTokenRepository.removeTopByTokenvalidationinfoOrderByLastUsedAtAsc(invalidToken.tokenvalidationinfo!!)
            }
        }

        val newToken = UserToken(createToken(), user, clock.now())
        userTokenRepository.save(newToken)
        return newToken
    }

    private fun createToken(): String =
        ByteArray(TOKEN_BYTE_SIZE).let { byteArray ->
            SecureRandom.getInstanceStrong().nextBytes(byteArray)
            Base64.getUrlEncoder().encodeToString(byteArray)
        }

    companion object {
        private const val TOKEN_BYTE_SIZE = 256 / 8
    }
}

sealed class CreateUserInfo {
    object UnsafePassword : CreateUserInfo()
    object UserAlreadyExists : CreateUserInfo()
    data class UserCreated(val user: User) : CreateUserInfo()
}

sealed class GetUserInfo {
    object UserNotFound : GetUserInfo()
    object AuthenticationFailed : GetUserInfo()
    data class UserFound(val user: User) : GetUserInfo()
}

sealed class CreateUserTokenInfo {
    object AuthenticationFailed : CreateUserTokenInfo()
    data class TokenCreated(val token: UserToken) : CreateUserTokenInfo()
}

sealed class ValidateUserTokenInfo {
    object AuthenticationFailed : ValidateUserTokenInfo()
    data class TokenValid(val user: User) : ValidateUserTokenInfo()
}

sealed class CreateRefreshTokenInfo {
    object TokenAlreadyExists : CreateRefreshTokenInfo()
    object TokenCreated : CreateRefreshTokenInfo()
}

sealed class RemoveRefreshTokenInfo {
    object AccountNotLinked : RemoveRefreshTokenInfo()
    object AccountUnlinked : RemoveRefreshTokenInfo()
}

sealed class UpdateRefreshTokenInfo {
    object UserNotFound : UpdateRefreshTokenInfo()
    object TokenUpdated : UpdateRefreshTokenInfo()
}

sealed class GetRefreshTokenInfo {
    object UserNotFound : GetRefreshTokenInfo()
    object TokenNotFound : GetRefreshTokenInfo()
    data class TokenFound(val refreshToken: RefreshToken) : GetRefreshTokenInfo()
}