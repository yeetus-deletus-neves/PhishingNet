package phishingnet.contentAnalysis.models

import phishingnet.contentAnalysis.models.warnings.WarningLog

interface AnalysisModule {
    val name: String

    //A análise de contúdo analisa o body já limpo de tags de html
    fun process(email: Email): WarningLog
}