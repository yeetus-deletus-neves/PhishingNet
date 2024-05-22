package com.example.demo.services.analysisServices

import com.example.demo.contentAnalysis.models.Email
import com.example.demo.data.entities.RefreshToken
import com.example.demo.data.entities.User
import com.example.demo.data.repositories.RefreshTokenRepository
import com.example.demo.security.SymmetricEncoder
import org.springframework.stereotype.Service
import java.util.MissingResourceException

@Service
class AnalysisServicesImpl(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val symmetricEncoder: SymmetricEncoder
): AnalysisServices {

    override fun analyseMessage(user: User, messageID: String): AnalysisResult {
        if (user.linked_email!!.isEmpty()) return AnalysisResult.AccountNotLinked
        try {
            val retrievedRefresh = refreshTokenRepository.findRefreshTokensByUserid(user) ?: throw Exception("Unable to find refresh token")
            val decodedRefresh = symmetricEncoder.decode(retrievedRefresh.rtoken)

        }catch (e: Exception){
            //TODO(More catches are needed, more precise error handling)
            return AnalysisResult.UnableToRetrieveRefresh(e.stackTraceToString())
        }
        val retrievedRefreshToken = refreshTokenRepository.findRefreshTokensByUserid(user)
        return AnalysisResult.CompletedAnalysis("TODO")
    }

    private fun getEmail(refreshToken: String): Email {
        TODO()
    }
}
sealed class AnalysisResult{
    data class CompletedAnalysis(val result: String): AnalysisResult()
    object AccountNotLinked: AnalysisResult()
    data class UnableToRetrieveRefresh(val error: String): AnalysisResult()
}