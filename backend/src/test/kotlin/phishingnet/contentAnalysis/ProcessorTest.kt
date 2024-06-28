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

    //maybe add various possible evaluations for each risk, without needing to create a new risk
    private val processor = Processor(
        listOf(
            FromHistoryModule(),
            HeaderAuthModule(),
            ReturnPathModule(),
            IbanDetectionModule(),
        ), mutableListOf(
            Risk(
                "Sender might be trying to impersonate someone else",
                "Email sender might be trying to impersonate someone you know",
                RiskLevel.B,
                warningRequirements = mutableMapOf(
                    Warning.FROM_DISTINCT_FROM_RETURN_PATH to reqExactOccurrence
                ),
            ),Risk(
                "Sender might be trying to impersonate someone else",
                "Email sender might be trying to impersonate someone you know",
                RiskLevel.C,
                warningRequirements = mutableMapOf(
                    Warning.HEADER_CERTIFICATES_AUTH_FAILED to reqExactOccurrence
                ),
            ),Risk(
                "Sender might be trying to impersonate someone else",
                "Email sender might be trying to impersonate someone you know",
                RiskLevel.E,
                warningRequirements = mutableMapOf(
                    Warning.FROM_DISTINCT_FROM_RETURN_PATH to reqExactOccurrence,
                    Warning.HEADER_CERTIFICATES_AUTH_FAILED to reqExactOccurrence
                ),
            ), Risk(
                "Possible financial scam",
                "The email comes from a new contact and contains an IBAN",
                RiskLevel.E,
                warningRequirements = mutableMapOf(
                    Warning.ASKS_FOR_IBAN to reqMinOccurrence,
                    Warning.PAST_EMAILS_SENT to Requirement(maximum = 3)
                )
            ), Risk(
                "Urgency",
                "Email is marked as urgent",
                RiskLevel.C,
                warningRequirements = mutableMapOf(Warning.URGENCY to reqExactOccurrence)
            )
        )
    )


    @Test
    fun `evaluate phishing email`() {
        val eval = processor.process(listOf(realPhishingEmail1))

        val expectedAnalysisEntry = RiskAnalysisEntry(
            "Sender might be trying to impersonate someone else",
            "Email sender might be trying to impersonate someone you know",
            RiskLevel.C
        )

        val expectedEvaluation = RiskAnalysis(RiskLevel.C, listOf(expectedAnalysisEntry))

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }

    @Test
    fun `evaluate real promotional email`() {
        val eval = processor.process(listOf(realPromotionalEmail))

        val expectedEvaluation = RiskAnalysis(
            RiskLevel.B,
            listOf(
                RiskAnalysisEntry(
                    "Sender might be trying to impersonate someone else",
                    "Email sender might be trying to impersonate someone you know",
                    threat=RiskLevel.B
                )
            )
        )

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }

    @Test
    fun `evaluate test empty email`() {
        val eval = processor.process(listOf(realPromotionalEmail))

        val expectedEvaluation = RiskAnalysis(
            RiskLevel.B,
            listOf(
                RiskAnalysisEntry(
                    "Sender might be trying to impersonate someone else",
                    "Email sender might be trying to impersonate someone you know",
                    RiskLevel.B
                )
            )
        )

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }

    @Test
    fun `evaluate test email with bad headers`() {
        val eval = processor.process(listOf(testEmailWithBadHeaders))

        val expectedAnalysisEntry = RiskAnalysisEntry(
            "Sender might be trying to impersonate someone else",
            "Email sender might be trying to impersonate someone you know",
            RiskLevel.C
        )

        val expectedEvaluation = RiskAnalysis(RiskLevel.C, listOf(expectedAnalysisEntry))

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }
    @Test
    fun `evaluate test email with no reason to be flagged`() {
        val eval = processor.process(listOf(testEmail))

        val expectedEvaluation = RiskAnalysis(RiskLevel.A, listOf())

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }

}