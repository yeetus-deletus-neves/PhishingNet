package phishingnet.api.services.analysisServices;

import phishingnet.api.data.entities.User

interface AnalysisServices {
    fun analyseMessage(user: User, messageID: String): AnalysisResult
}
