package com.example.demo.services.userServices

import com.example.demo.data.repositories.UsersRepository
import com.example.demo.data.entities.RefreshToken
import com.example.demo.data.entities.User
import com.example.demo.data.entities.UserToken
import com.example.demo.data.repositories.RefreshTokenRepository
import com.example.demo.data.repositories.UserTokenRepository
import com.example.demo.utils.Clock
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*

@Service
class UserServicesImpl(
    private val usersRepository: UsersRepository,
    private val userTokenRepository: UserTokenRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val clock: Clock
): UserServices {

    override fun createUser(username: String, password: String): CreateUserInfo {
        if (!isPasswordSecure(password)) return CreateUserInfo.UnsafePassword
        if (usersRepository.getUserByUsername(username) != null) return CreateUserInfo.UserAlreadyExists

        //TODO: Password encrypt
        val newUser = usersRepository.createUser(username, password)
        return CreateUserInfo.UserCreated(newUser!!)
    }

    override fun getUserById(userID: UUID): GetUserInfo {
        val user = usersRepository.getUserById(userID) ?: return  GetUserInfo.UserNotFound
        return GetUserInfo.UserFound(user)
    }

    override fun getUserByToken(token: String): GetUserInfo {
        val user = usersRepository.getUserByToken(token) ?: return  GetUserInfo.UserNotFound
        return GetUserInfo.UserFound(user)
    }

    override fun createUserToken(username: String, password: String): CreateUserTokenInfo {
        //TODO: Gerenciamento de tokens (tempo e numero)
        val user = usersRepository.getUserByUsername(username) ?: return  CreateUserTokenInfo.AuthenticationFailed
        if (password != user.password) return CreateUserTokenInfo.AuthenticationFailed

        val newToken = userTokenRepository.createUserToken(user.userID, createToken(), clock.now())
        return CreateUserTokenInfo.TokenCreated(newToken!!)
    }

    override fun validateUserToken(token: String): ValidateUserTokenInfo {
        val myToken = userTokenRepository.getUserToken(token) ?: return ValidateUserTokenInfo.AuthenticationFailed
        val user = usersRepository.getUserById(myToken.userID) ?: return ValidateUserTokenInfo.AuthenticationFailed

        return ValidateUserTokenInfo.TokenValid(user)
    }

    override fun updateRefreshToken(userToken: String, newToken: String): UpdateRefreshTokenInfo {
        val user = usersRepository.getUserByToken(userToken) ?: return  UpdateRefreshTokenInfo.AuthenticationFailed
        refreshTokenRepository.updateRefreshToken(user.userID, newToken)

        return UpdateRefreshTokenInfo.TokenUpdated
    }

    override fun getRefreshToken(userToken: String): GetRefreshTokenInfo {
        val user = usersRepository.getUserByToken(userToken) ?: return  GetRefreshTokenInfo.AuthenticationFailed
        val token = refreshTokenRepository.getRefreshToken(user.userID) ?: return GetRefreshTokenInfo.TokenNotFound

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

    fun createToken(): String =
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

sealed class UpdateRefreshTokenInfo {
    object AuthenticationFailed : UpdateRefreshTokenInfo()
    object TokenUpdated : UpdateRefreshTokenInfo()
}

sealed class GetRefreshTokenInfo {
    object AuthenticationFailed : GetRefreshTokenInfo()
    object TokenNotFound : GetRefreshTokenInfo()
    data class TokenFound(val refreshToken: RefreshToken) : GetRefreshTokenInfo()
}