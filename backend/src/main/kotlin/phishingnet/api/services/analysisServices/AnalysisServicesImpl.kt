package phishingnet.api.services.analysisServices

import phishingnet.api.data.entities.RefreshToken
import phishingnet.api.data.entities.User
import phishingnet.api.data.repositories.RefreshTokenRepository
import phishingnet.api.data.repositories.UsersRepository
import phishingnet.api.security.SymmetricEncoder
import phishingnet.api.utils.graph.GraphInterface
import org.springframework.stereotype.Service

@Service
class AnalysisServicesImpl(
    private val usersRepository: UsersRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val symmetricEncoder: SymmetricEncoder
): AnalysisServices {

    private final val graphInterface: GraphInterface = GraphInterface()

    override fun analyseMessage(user: User, messageID: String): AnalysisResult {
        try {
            if (user.linked_email!!.isEmpty()) return AnalysisResult.AccountNotLinked

            val retrievedRefresh = refreshTokenRepository.findRefreshTokensByUserid(user)
                ?: throw Exception("Unable to find refresh token")
            val decodedRefresh = symmetricEncoder.decode(retrievedRefresh.rtoken)
                ?: throw Exception("Unable to decrypt refresh token from DB")

            val graphTokens = graphInterface.getTokensFromRefresh(decodedRefresh)
                ?: throw Exception("Unable to get graph tokens")
            updateRefreshToken(retrievedRefresh, graphTokens.refreshToken)

            val emailDetails = graphInterface.getEmailDetails(messageID, graphTokens.accessToken)
                ?: throw Exception("Unable to get email details")

            return AnalysisResult.CompletedAnalysis(emailDetails.messageInfo.body.content)
        }
        catch (e: IllegalArgumentException) {
            return AnalysisResult.BadRequest(e.stackTraceToString())
        }
        catch (e: SecurityException) {
            unlinkUser(user)
            return AnalysisResult.InvalidToken(e.stackTraceToString())
        }
        catch (e: NoSuchElementException) {
            return AnalysisResult.MessageNotFound(e.stackTraceToString())
        }
        catch (e: Exception){
            return AnalysisResult.UnexpectedError(e.stackTraceToString())
        }
    }

    private fun updateRefreshToken(refreshToken: RefreshToken, newValue: String) {
        refreshToken.rtoken = newValue
        refreshTokenRepository.save(refreshToken)
    }

    private fun unlinkUser(user: User) {
        user.linked_email = null
        usersRepository.save(user)

        val refreshToken = refreshTokenRepository.findRefreshTokensByUserid(user) ?: return
        refreshTokenRepository.delete(refreshToken)
    }
}
sealed class AnalysisResult{
    data class CompletedAnalysis(val result: String): AnalysisResult()
    object AccountNotLinked: AnalysisResult()
    data class BadRequest(val log: String): AnalysisResult()
    data class InvalidToken(val log: String): AnalysisResult()
    data class MessageNotFound(val log: String): AnalysisResult()
    data class UnexpectedError(val log: String): AnalysisResult()
}