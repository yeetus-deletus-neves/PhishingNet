package phishingnet.contentAnalysis

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysis
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.Requirement
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.modules.*

/***
 * NOTES FROM TESTS
 * removed Warning.FROM_DISTINCT_FROM_RETURN_PATH from Email sender suspicious
 * removed Grammatical errors risk, some emails keep some html tags, and will be illegitimately flagged for errors
 *
 */
class ProcessorTest {
    private val reqMinOccurrence = Requirement(minimum = 1)
    private val reqExactOccurrence = Requirement(exact = 1)

    private val processor = Processor(
        listOf(
            FromHistoryModule(),
            HeaderAuthModule(),
            ReturnPathModule(),
            IbanDetectionModule(),
        ), mutableListOf(
            Risk(
                "Email sender suspicious",
                "Email sender might be trying to impersonate someone you know.",
                RiskLevel.SUSPICIOUS,
                warningRequirements = mutableMapOf(
                    Warning.HEADER_CERTIFICATES_AUTH_FAILED to reqExactOccurrence
                ),
            ), Risk(
                "Possible financial scam",
                "The email comes from a new contact and contains an IBAN.",
                RiskLevel.ALARMING,
                warningRequirements = mutableMapOf(
                    Warning.ASKS_FOR_IBAN to reqMinOccurrence,
                    Warning.PAST_EMAILS_SENT to Requirement(maximum = 3)
                )
            ), Risk(
                "Urgency",
                "Email is marked as urgent",
                RiskLevel.SUSPICIOUS,
                warningRequirements = mutableMapOf(Warning.URGENCY to reqExactOccurrence)
            )
        )
    )


    @Test
    fun `evaluate phishing email`() {
        val eval = processor.process(listOf(realPhishingEmail1))

        val expectedAnalysisEntry = RiskAnalysisEntry("Email sender suspicious", "Email sender might be trying to impersonate someone you know.", RiskLevel.SUSPICIOUS)
        val expectedEvaluation = RiskAnalysis(RiskLevel.SUSPICIOUS, listOf(expectedAnalysisEntry))

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }

    @Test
    fun `evaluate real promotional email`() {
        val eval = processor.process(listOf(realPromotionalEmail))

        val expectedEvaluation = RiskAnalysis(RiskLevel.NO_THREAT, listOf())

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }

    @Test
    fun `evaluate test empty email`() {
        val eval = processor.process(listOf(realPromotionalEmail))

        val expectedEvaluation = RiskAnalysis(RiskLevel.NO_THREAT, listOf())

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }

    @Test
    fun `evaluate test email with bad headers`() {
        val eval = processor.process(listOf(testEmailWithBadHeaders))

        val expectedAnalysisEntry = RiskAnalysisEntry("Email sender suspicious", "Email sender might be trying to impersonate someone you know.", RiskLevel.SUSPICIOUS)
        val expectedEvaluation = RiskAnalysis(RiskLevel.SUSPICIOUS, listOf(expectedAnalysisEntry))

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }
    @Test
    fun `evaluate test email with no reason to be flagged`() {
        val eval = processor.process(listOf(testEmail))

        val expectedEvaluation = RiskAnalysis(RiskLevel.NO_THREAT, listOf())

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }

}