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


class ProcessorTest {

    private val processor = Processor(
        listOf(
            FromHistoryModule(),
            HeaderAuthModule(),
            ReturnPathModule(),
            IbanDetectionModule(),
            LanguageToolModule(),
            UrgencyModule()
        ), mutableListOf(
            Risk(
                "Sender might be trying to impersonate someone else",
                "Email sender might be trying to impersonate someone you know",
                RiskLevel.B,
                warningRequirements = mutableMapOf(
                    Warning.FROM_DISTINCT_RETURN_PATH to Requirement(exact = 1)
                ),
            ), Risk(
                "Sender might be trying to impersonate someone else",
                "Email sender might be trying to impersonate someone you know",
                RiskLevel.C,
                warningRequirements = mutableMapOf(
                    Warning.HEADER_CERTIFICATES_AUTH_FAILED to Requirement(exact = 1)
                ),
            ), Risk(
                "Sender might be trying to impersonate someone else",
                "Email sender might be trying to impersonate someone you know",
                RiskLevel.E,
                warningRequirements = mutableMapOf(
                    Warning.FROM_DISTINCT_RETURN_PATH to Requirement(exact = 1),
                    Warning.HEADER_CERTIFICATES_AUTH_FAILED to Requirement(exact = 1)
                ),
            ), Risk(
                "Possible financial scam",
                "The email comes from a new contact and contains an IBAN",
                RiskLevel.E,
                warningRequirements = mutableMapOf(
                    Warning.ASKS_FOR_IBAN to Requirement(minimum = 1),
                    Warning.PAST_EMAILS_SENT to Requirement(maximum = 3)
                )
            ), Risk(
                "Urgency",
                "Email is marked as urgent",
                RiskLevel.C,
                warningRequirements = mutableMapOf(Warning.URGENCY to Requirement(exact = 1))
            ), Risk(
                "Possible Bad Grammar",
                "Several instances of bad grammar, however this might be due to the way the email is formatted",
                RiskLevel.B,
                warningRequirements = mutableMapOf(Warning.BAD_GRAMMAR to Requirement(minimum = 3))
            )
        )
    )

    private val expectedAnalysisEntryImpersonationC =
        RiskAnalysisEntry(
            "Sender might be trying to impersonate someone else",
            "Email sender might be trying to impersonate someone you know",
            RiskLevel.C
        )

    private val expectedAnalysisEntryImpersonationB =
        RiskAnalysisEntry(
            "Sender might be trying to impersonate someone else",
            "Email sender might be trying to impersonate someone you know",
            threat = RiskLevel.B
        )

    private val expectedAnalysisEntryBadGrammar =
        RiskAnalysisEntry(
            "Possible Bad Grammar",
            "Several instances of bad grammar, however this might be due to the way the email is formatted",
            RiskLevel.B
        )

    @Test
    fun `evaluate phishing email`() {
        val eval = processor.process(listOf(realPhishingEmail1))

        val expectedEvaluation = RiskAnalysis(RiskLevel.C, listOf(expectedAnalysisEntryImpersonationC))

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }

    @Test
    fun `evaluate real promotional email`() {
        val eval = processor.process(listOf(realPromotionalEmail))

        val expectedEvaluation =
            RiskAnalysis(RiskLevel.B, listOf(expectedAnalysisEntryImpersonationB, expectedAnalysisEntryBadGrammar))

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }

    @Test
    fun `evaluate test empty email`() {
        val eval = processor.process(listOf(realPromotionalEmail))

        val expectedEvaluation =
            RiskAnalysis(RiskLevel.B, listOf(expectedAnalysisEntryImpersonationB, expectedAnalysisEntryBadGrammar))

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }

    @Test
    fun `evaluate test email with bad headers`() {
        val eval = processor.process(listOf(testEmailWithBadHeaders))

        val expectedEvaluation = RiskAnalysis(RiskLevel.C, listOf(expectedAnalysisEntryImpersonationC))

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