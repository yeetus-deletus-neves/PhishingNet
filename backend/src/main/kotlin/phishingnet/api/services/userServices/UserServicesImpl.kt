package phishingnet.api.services.userServices

import phishingnet.api.data.entities.RefreshToken
import phishingnet.api.data.entities.User
import phishingnet.api.data.entities.UserToken
import phishingnet.api.data.repositories.RefreshTokenRepository
import phishingnet.api.data.repositories.UsersRepository
import phishingnet.api.data.repositories.UserTokenRepository
import phishingnet.api.security.SaltPepperEncoder
import phishingnet.api.security.SymmetricEncoder
import phishingnet.api.utils.Clock
import phishingnet.api.security.TokenEncoder
import phishingnet.api.utils.graph.GraphInterface
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.util.*

@Service
class UserServicesImpl(
    private val usersRepository: UsersRepository,
    private val userTokenRepository: UserTokenRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val saltPepperEncoder: SaltPepperEncoder,
    private val tokenEncoder : TokenEncoder,
    private val symmetricEncoder: SymmetricEncoder,
    private val clock: Clock
): UserServices {

    private final val graphInterface: GraphInterface = GraphInterface()

    @Transactional
    override fun createUser(username: String, password: String): CreateUserInfo {
        if (!isPasswordSecure(password)) return CreateUserInfo.UnsafePassword
        if (usersRepository.findUserByUsername(username) != null) return CreateUserInfo.UserAlreadyExists

        val hashToken = saltPepperEncoder.encode(password)
        val newUser = User(username, hashToken.encodedValue, hashToken.salt)
        usersRepository.save(newUser)
        return CreateUserInfo.UserCreated(newUser)
    }

    @Transactional(readOnly = true)
    override fun getUserById(userID: String): GetUserInfo {
        try{
            val id = UUID.fromString(userID)
            val user = usersRepository.findUserById(id) ?: return GetUserInfo.UserNotFound
            return GetUserInfo.UserFound(user)
        }catch (e: IllegalArgumentException){
            return GetUserInfo.InvalidID
        }
    }

    @Transactional(readOnly = true)
    override fun getUserByToken(token: String): GetUserInfo {
        val encodedToken = tokenEncoder.encode(token)

        val myToken = userTokenRepository.findUserTokenByTokenvalidationinfo(encodedToken) ?: return GetUserInfo.UserNotFound
        return GetUserInfo.UserFound(myToken.userid)
    }

    @Transactional
    override fun createUserToken(username: String, password: String): CreateUserTokenInfo {
        val user = usersRepository.findUserByUsername(username)
        if ( user == null || !saltPepperEncoder.validate(password, user.passwordSalt, user.passwordinfo)) return CreateUserTokenInfo.AuthenticationFailed

        val newToken = getToken(user)
        return CreateUserTokenInfo.TokenCreated(user, newToken)
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

        val rToken =  graphInterface.getRefreshFromAccess(token)  ?: return CreateRefreshTokenInfo.InvalidToken
        val email = graphInterface.getEmailAddress(rToken.accessToken) ?: return CreateRefreshTokenInfo.UnableToObtainUserInformation
        user.linked_email = email

        refreshTokenRepository.save(
            RefreshToken(user, symmetricEncoder.encode(rToken.refreshToken)!!)
        )
        usersRepository.save(user)
        return CreateRefreshTokenInfo.TokenCreated
    }

    @Transactional
    override fun removeRefreshToken(user: User): RemoveRefreshTokenInfo {
        val myToken = refreshTokenRepository.findRefreshTokensByUserid(user) ?: return RemoveRefreshTokenInfo.AccountNotLinked
        user.linked_email = null

        refreshTokenRepository.delete(myToken)
        usersRepository.save(user)
        return RemoveRefreshTokenInfo.AccountUnlinked
    }

    @Transactional
    override fun updateRefreshToken(user: User, newToken: String): UpdateRefreshTokenInfo {
        val token = refreshTokenRepository.findRefreshTokensByUserid(user) ?: return UpdateRefreshTokenInfo.UserNotFound
        token.rtoken = symmetricEncoder.encode(newToken)!!

        refreshTokenRepository.save(token)

        return UpdateRefreshTokenInfo.TokenUpdated
    }

    @Transactional(readOnly = true)
    override fun getRefreshToken(user: User): GetRefreshTokenInfo {
        val token = refreshTokenRepository.findRefreshTokensByUserid(user) ?: return GetRefreshTokenInfo.TokenNotFound
        token.rtoken = symmetricEncoder.decode(token.rtoken)!!
        return GetRefreshTokenInfo.TokenFound(token)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     FUNÇÕES AUXILIARES                                     //
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

        // Se já existir o número máximo de tokens, elimina o último utilizado
        if (tokenCount >= UserToken.MAX_TOKENS) {
            val toDelete = tokenCount - UserToken.MAX_TOKENS + 1
            for (n in 1..toDelete) {
                val invalidToken = userTokenRepository.findTopByUseridOrderByLastUsedAtAsc(user) ?: continue
                userTokenRepository.removeTopByTokenvalidationinfoOrderByLastUsedAtAsc(invalidToken.tokenvalidationinfo)
            }
        }
        val tokenValue = createToken()
        val creationInstant = clock.now()

        // The token present on the DB is encoded
        val encodedToken = UserToken(tokenEncoder.encode(tokenValue), user, creationInstant)
        userTokenRepository.save(encodedToken)

        return UserToken(tokenValue, user, creationInstant)
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
    object InvalidID : GetUserInfo()
    object AuthenticationFailed : GetUserInfo()
    data class UserFound(val user: User) : GetUserInfo()
}

sealed class CreateUserTokenInfo {
    object AuthenticationFailed : CreateUserTokenInfo()
    data class TokenCreated(val user: User, val token: UserToken) : CreateUserTokenInfo()
}

sealed class ValidateUserTokenInfo {
    object AuthenticationFailed : ValidateUserTokenInfo()
    data class TokenValid(val user: User) : ValidateUserTokenInfo()
}

sealed class CreateRefreshTokenInfo {
    object TokenAlreadyExists : CreateRefreshTokenInfo()
    object InvalidToken : CreateRefreshTokenInfo()
    object TokenCreated : CreateRefreshTokenInfo()
    object UnableToObtainUserInformation : CreateRefreshTokenInfo()
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