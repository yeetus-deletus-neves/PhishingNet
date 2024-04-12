package com.example.demo.services.userServices

import com.example.demo.data.UsersRepository
import com.example.demo.data.entities.RefreshToken
import com.example.demo.data.entities.User
import com.example.demo.data.entities.UserToken
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*

@Service
class UserServicesImpl(
    private val usersRepository: UsersRepository
): UserServices {

    override fun createUser(username: String, password: String): CreateUserInfo {
        if (!isPasswordSecure(password)) return CreateUserInfo.UnsafePassword
        if (usersRepository.getUserByUsername(username) != null) return CreateUserInfo.UserAlreadyExists

        //TODO: Password encrypt
        val newUser = usersRepository.createUser(username, password)
        return CreateUserInfo.UserCreated(newUser!!.userID)
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

        val newToken = usersRepository.createUserToken(user.userID, createToken())
        return CreateUserTokenInfo.TokenCreated(newToken!!)
    }

    override fun validateUserToken(userID: UUID, token: String): ValidateUserTokenInfo {
        val token = usersRepository.getUserToken(token) ?: return ValidateUserTokenInfo.AuthenticationFailed
        if (token.userID != userID) return ValidateUserTokenInfo.AuthenticationFailed

        return ValidateUserTokenInfo.TokenValid(token.userID)
    }

    // TODO: NAO DEVE ESTAR EXPOSTA NA API. ESTA FUNCAO DEVE SER AUXILIAR PARA OUTRAS FUNCOES REFERETES A LOGICA
    override fun updateRefreshToken(userToken: String, newToken: String): UpdateRefreshTokenInfo {
        val user = usersRepository.getUserByToken(userToken) ?: return  UpdateRefreshTokenInfo.AuthenticationFailed
        usersRepository.updateRefreshToken(user.userID, newToken)

        return UpdateRefreshTokenInfo.TokenUpdated
    }

    override fun getRefreshToken(userToken: String): GetRefreshTokenInfo {
        TODO("Not yet implemented")
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
    data class UserCreated(val userID: UUID) : CreateUserInfo()
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
    data class TokenValid(val userID: UUID) : ValidateUserTokenInfo()
}

sealed class UpdateRefreshTokenInfo {
    object AuthenticationFailed : UpdateRefreshTokenInfo()
    object TokenUpdated : UpdateRefreshTokenInfo()
}

sealed class GetRefreshTokenInfo {
    object AuthenticationFailed : GetRefreshTokenInfo()
    data class TokenFound(val refreshToken: RefreshToken) : GetRefreshTokenInfo()
}