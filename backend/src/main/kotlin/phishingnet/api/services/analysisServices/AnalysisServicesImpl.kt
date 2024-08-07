package phishingnet.api.services.analysisServices

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import phishingnet.api.data.entities.RefreshToken
import phishingnet.api.data.entities.User
import phishingnet.api.data.repositories.RefreshTokenRepository
import phishingnet.api.data.repositories.UsersRepository
import phishingnet.api.security.SymmetricEncoder
import phishingnet.api.utils.graph.GraphInterface
import org.springframework.stereotype.Service
import phishingnet.api.data.entities.CacheID
import phishingnet.api.data.entities.UserCache
import phishingnet.api.data.repositories.UserCacheRepository
import phishingnet.api.utils.graph.models.GraphEmailDetails
import phishingnet.contentAnalysis.Processor
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.Sender
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysis
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.RiskLevel

@Service
class AnalysisServicesImpl(
    private val usersRepository: UsersRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userCacheRepository: UserCacheRepository,
    private val symmetricEncoder: SymmetricEncoder,
    private val analysisUnit: Processor
): AnalysisServices {

    private final val graphInterface: GraphInterface = GraphInterface()

    override fun analyseMessage(user: User, messageID: String): AnalysisResult {
        try {
            if (user.linked_email!!.isEmpty()) return AnalysisResult.AccountNotLinked

            val cacheEmail = userCacheRepository.findUserCacheByIdAndConversationid(user.id, messageID)
            if (cacheEmail != null) {
                return AnalysisResult.WasInCache(cacheEmail.threat)
            }

            val retrievedRefresh = refreshTokenRepository.findRefreshTokensByUserid(user)
                ?: throw Exception("Não foi possível encontrar o refresh token")
            val decodedRefresh = symmetricEncoder.decode(retrievedRefresh.rtoken)
                ?: throw Exception("Não foi possível desencriptar o refresh token da base de dados")

            val graphTokens = graphInterface.getTokensFromRefresh(decodedRefresh)
                ?: throw Exception("Não foi possível obter graph tokens")
            updateRefreshToken(retrievedRefresh, graphTokens.refreshToken)

            val emailDetails = graphInterface.getEmailDetails(messageID, graphTokens.accessToken)
                ?: throw Exception("Não foi possível obter os detalhes do email")

            val senderHistory = graphInterface.countSenderHistory(emailDetails[0].messageInfo.from.emailAddress.address, graphTokens.accessToken)
                ?: throw Exception("Não foi possível obter os detalhes do email")

            val compiledEmail = emailDetails.map { compileMessageInfo(it, senderHistory) }.filter { it.from.address != user.linked_email }

            val res = analysisUnit.process(compiledEmail)
            
            userCacheRepository.saveAndFlush(UserCache(CacheID( user.id, messageID),Json.encodeToString(res)))
            val count = userCacheRepository.countUserCachesById(user.id)
            if (count > 10){
                val firstOut = userCacheRepository.getFirstById(user.id)
                if (firstOut != null){
                    userCacheRepository.removeByIdAndConversationid(user.id,firstOut.conversationid)
                }
            }

            return AnalysisResult.CompletedAnalysis(res)
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

    private fun compileMessageInfo(message: GraphEmailDetails, fromEmailCount: Int): Email{
        return Email(
            from = Sender(message.messageInfo.from.emailAddress.name, message.messageInfo.from.emailAddress.address),
            fromEmailCount = fromEmailCount,
            sender = Sender(
                message.messageInfo.sender.emailAddress.name,
                message.messageInfo.sender.emailAddress.address
            ),
            subject = message.messageInfo.subject,
            importance = message.messageInfo.importance,
            hasAttachments = message.messageInfo.hasAttachments,
            isRead = message.messageInfo.isRead,
            returnPath = message.headers.internetMessageHeaders?.find { it.name == "Return-Path" }?.value,
            rawAuthResults = message.headers.internetMessageHeaders?.find { it.name == "Authentication-Results" }?.value,
            rawBody = message.messageInfo.body.content,
            attachments = message.attachments.value.map { it.name }
        )
    }

}
sealed class AnalysisResult{
    data class CompletedAnalysis(val result: RiskAnalysis): AnalysisResult()
    data class WasInCache(val result:String):AnalysisResult()
    object NoMessageToBeAnalyzed: AnalysisResult()
    object AccountNotLinked: AnalysisResult()
    data class BadRequest(val log: String): AnalysisResult()
    data class InvalidToken(val log: String): AnalysisResult()
    data class MessageNotFound(val log: String): AnalysisResult()
    data class UnexpectedError(val log: String): AnalysisResult()
}