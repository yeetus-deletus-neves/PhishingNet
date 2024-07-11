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
import phishingnet.contentAnalysis.modules.experimental_modules.LLMModule


class ProcessorTest {

    private val processor = Processor(
        listOf(
            FromHistoryModule(),
            HeaderAuthModule(),
            ReturnPathModule(),
            IbanDetectionModule(),
            LanguageToolModule(),
            UrgencyModule(),
            AttachmentExtensionModule(),
            BlackListedTinyUrlModule(),
            GoogleSafeBrowsingApi(),
            LLMModule()
        ), listOf(
            Risk(
                "O remetente pode se estar a tentar passar por outra pessoa",
                "O remetente do email é diferente do caminho de retorno, " +
                        "pode ser indicativo de uma tentativa de falsificação, " +
                        "mas é também uma prática comum em emails empresariais e em particular emails comerciais.",
                RiskLevel.B,
                warningRequirements = mutableMapOf(
                    Warning.FROM_DISTINCT_RETURN_PATH to Requirement(exact = 1)
                ),
            ),
            Risk(
                "O remetente pode se estar a tentar passar por outra pessoa",
                "Os protocolos de autenticação DKIM e/ou SPF falharam.",
                RiskLevel.D,
                warningRequirements = mutableMapOf(
                    Warning.HEADER_CERTIFICATES_AUTH_FAILED to Requirement(minimum = 1)
                ),
            ),
            Risk(
                "O remetente pode se estar a tentar passar por outra pessoa",
                "Os protocolos de autenticação DKIM e/ou SPF falharam, " +
                        "e o remetente de email é diferente do caminho de retorno.",
                RiskLevel.E,
                warningRequirements = mutableMapOf(
                    Warning.FROM_DISTINCT_RETURN_PATH to Requirement(exact = 1),
                    Warning.HEADER_CERTIFICATES_AUTH_FAILED to Requirement(minimum = 1)
                ),
            ),
            Risk(
                "O email pode ter sido comprometido no caminho entre o remetente e o recetor",
                "Os protocolo de autenticação DKIM falhou.",
                RiskLevel.C,
                warningRequirements = mutableMapOf(
                    Warning.DKIM_AUTH_FAILED to Requirement(exact = 1)
                ),
            ),
            Risk(
                "Possível burla financeira",
                "Este remetente não tem um historial de troca de emails consigo e contém um IBAN, " +
                        "informe-se antes de efetuar pagamentos.",
                RiskLevel.C,
                warningRequirements = mutableMapOf(
                    Warning.ASKS_FOR_IBAN to Requirement(minimum = 1),
                    Warning.PAST_EMAILS_SENT to Requirement(maximum = 3)
                )
            ),
            Risk(
                "Possível burla financeira",
                "O email está marcado como urgente e contém um IBAN, potencialmente de forma a o apressar " +
                        "a efetuar um pagamento informe-se antes de efetuar pagamentos.",
                RiskLevel.E,
                warningRequirements = mutableMapOf(
                    Warning.ASKS_FOR_IBAN to Requirement(minimum = 1),
                    Warning.URGENCY to Requirement(exact = 1)
                )
            ),
            Risk(
                "Possível burla financeira",
                "Os protocolos de autenticação DKIM e/ou SPF falharam e contém um IBAN.",
                RiskLevel.F,
                warningRequirements = mutableMapOf(
                    Warning.ASKS_FOR_IBAN to Requirement(minimum = 1),
                    Warning.HEADER_CERTIFICATES_AUTH_FAILED to Requirement(minimum = 1)
                )
            ),
            Risk(
                "Erros gramaticais",
                "Vários casos de erros gramaticais, no entanto, isto pode se dever à formatação do email.",
                RiskLevel.B,
                warningRequirements = mutableMapOf(Warning.BAD_GRAMMAR to Requirement(minimum = 5))
            ),
            Risk(
                "Anexo potencialmente malicioso detetado",
                "Foi detetada a existência de anexos executáveis.",
                RiskLevel.D,
                warningRequirements = mutableMapOf(Warning.FILE_ATTACHED_CAN_BE_DANGEROUS to Requirement(minimum = 1))
            ),
            Risk(
                "Existem links encurtados no email",
                "Os links presentes no email têm um destino deconhecido, e por isso potencialmente perigoso",
                RiskLevel.D,
                warningRequirements = mutableMapOf(Warning.URL_SHORTENED to Requirement(minimum = 1))
            ),
            Risk(
                "Existem links maliciosos no email",
                "Os links presentes no email foram detetados como sendo maliciosos pela Google Safe API.",
                RiskLevel.F,
                warningRequirements = mutableMapOf(Warning.MALICIOUS_URL to Requirement(minimum = 1))
            ),
            Risk(
                "O modelo de Inteligência Artificial detetou uma possível ameaça de phishing",
                "O modelo de Inteligência Artificial detetou que o email analisado se assemelha a um email de phishing",
                RiskLevel.E,
                warningRequirements = mutableMapOf(Warning.LLM_TRIGGERED to Requirement(exact = 1))
            ),
        )
    )

    private val expectedAnalysisEntryImpersonationB =
        RiskAnalysisEntry(
            "O remetente pode se estar a tentar passar por outra pessoa",
            "O remetente do email é diferente do caminho de retorno, " +
                    "pode ser indicativo de uma tentativa de falsificação, mas é também uma prática comum em emails " +
                    "empresariais e em particular emails comerciais.",
            RiskLevel.B
        )

    private val expectedAnalysisEntryImpersonationD =
        RiskAnalysisEntry(
            "O remetente pode se estar a tentar passar por outra pessoa",
            "Os protocolos de autenticação DKIM e/ou SPF falharam.",
            threat = RiskLevel.D
        )

    private val expectedAnalysisEntryImpersonationE =
        RiskAnalysisEntry(
            "O remetente pode se estar a tentar passar por outra pessoa",
            "Os protocolos de autenticação DKIM e/ou SPF falharam, " +
                    "e o remetente de email é diferente do caminho de retorno.",
            threat = RiskLevel.E
        )

    private val expectedAnalysisEntryModifiedContents = RiskAnalysisEntry(
        "O email pode ter sido comprometido no caminho entre o remetente e o recetor",
        "Os protocolo de autenticação DKIM falhou.",
        threat = RiskLevel.C

    )

    //"Os protocolos de autenticação DKIM e/ou SPF falharam."
    private val expectedAnalysisEntryBadGrammar =
        RiskAnalysisEntry(
            "Erros gramaticais",
            "Vários casos de erros gramaticais, no entanto, isto pode se dever à formatação do email.",
            RiskLevel.B
        )

    @Test
    fun `evaluate phishing email`() {
        val eval = processor.process(listOf(realPhishingEmail1))

        val expectedEvaluation = RiskAnalysis(
            RiskLevel.D,
            listOf(expectedAnalysisEntryImpersonationD, expectedAnalysisEntryModifiedContents)
        )

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
        val eval = processor.process(listOf(testEmailEmpty))

        val expectedEvaluation = RiskAnalysis(RiskLevel.A, listOf())

        Assertions.assertEquals(expectedEvaluation.threat, eval.threat)
        Assertions.assertEquals(expectedEvaluation.threatJustification, eval.threatJustification)
    }

    @Test
    fun `evaluate test email with bad headers`() {
        val eval = processor.process(listOf(testEmailWithBadHeaders))

        val expectedEvaluation = RiskAnalysis(
            RiskLevel.D,
            listOf(expectedAnalysisEntryImpersonationD, expectedAnalysisEntryModifiedContents)
        )

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