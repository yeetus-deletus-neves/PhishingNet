package phishingnet.api.services.analysisServices

import phishingnet.api.data.entities.RefreshToken
import phishingnet.api.data.entities.User
import phishingnet.api.data.repositories.RefreshTokenRepository
import phishingnet.api.data.repositories.UsersRepository
import phishingnet.api.security.SymmetricEncoder
import phishingnet.api.utils.graph.GraphInterface
import org.springframework.stereotype.Service
import phishingnet.api.utils.graph.models.GraphEmailDetails
import phishingnet.contentAnalysis.Processor
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.MessageHeadersInfo
import phishingnet.contentAnalysis.models.Sender

@Service
class AnalysisServicesImpl(
    private val usersRepository: UsersRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val symmetricEncoder: SymmetricEncoder,
    private val analysisUnit: Processor
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

            val compiledEmail = compileMessageInfo(emailDetails)
            return AnalysisResult.CompletedAnalysis(
                analysisUnit.process(compiledEmail).toString()
            )
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
        refreshToken.rtoken = symmetricEncoder.encode(newValue)!!
        refreshTokenRepository.save(refreshToken)
    }

    private fun unlinkUser(user: User) {
        user.linked_email = null
        usersRepository.save(user)

        val refreshToken = refreshTokenRepository.findRefreshTokensByUserid(user) ?: return
        refreshTokenRepository.delete(refreshToken)
    }

    private fun compileMessageInfo(message: GraphEmailDetails): Email{

        val internetHeader = MessageHeadersInfo(
            message.headers.internetMessageHeaders.find { it.name == "Authentication-Results" }!!.value,
            message.headers.internetMessageHeaders.find { it.name == "From" }!!.value,
            message.headers.internetMessageHeaders.find { it.name == "Return-Path" }!!.value
        )

        return Email(
            Sender(message.messageInfo.from.emailAddress.name, message.messageInfo.from.emailAddress.address),
            Sender(message.messageInfo.sender.emailAddress.name, message.messageInfo.sender.emailAddress.address),
            message.messageInfo.subject,
            message.messageInfo.importance,
            message.messageInfo.hasAttachments,
            message.messageInfo.isRead,
            internetHeader,
            message.messageInfo.body.content
        )
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