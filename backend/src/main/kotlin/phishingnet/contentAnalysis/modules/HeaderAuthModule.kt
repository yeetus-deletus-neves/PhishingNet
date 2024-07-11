package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.SecurityVerification
import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.warnings.Warning

/***
 * O retorno deste Módulo é um inteiro com significado correspondente a um booleano
 */
class HeaderAuthModule : AnalysisModule {
    override val name: String = "Módulo de cabeçalho"
    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(
            listOf(
                Warning.HEADER_CERTIFICATES_AUTH_FAILED,
                Warning.DKIM_AUTH_FAILED,
                Warning.SPF_AUTH_FAILED,
                Warning.DMARC_AUTH_FAILED
            )
        )

        if (email.authDetails.dmarc == SecurityVerification.FAILED) warningLog.incrementOccurrences(Warning.DMARC_AUTH_FAILED)
        if (email.authDetails.dkim == SecurityVerification.FAILED) warningLog.incrementOccurrences(Warning.DKIM_AUTH_FAILED)
        if (email.authDetails.spf == SecurityVerification.FAILED) warningLog.incrementOccurrences(Warning.SPF_AUTH_FAILED)

        if (email.authDetails.dmarc == SecurityVerification.FAILED ||
            email.authDetails.dkim == SecurityVerification.FAILED ||
            email.authDetails.spf == SecurityVerification.FAILED
        ) warningLog.incrementOccurrences(Warning.HEADER_CERTIFICATES_AUTH_FAILED)

        return warningLog
    }
}