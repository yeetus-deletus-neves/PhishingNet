package phishingnet.contentAnalysis.modules

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phishingnet.contentAnalysis.Processor
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysisEntry
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.realPromotionalEmail
import phishingnet.contentAnalysis.testEmail

class LanguageToolModuleModuleTest {

    private val mockRisk =
        Risk("Incorrect Grammar", "email is not properly written", RiskLevel.C)
            .apply { setRequirement(Warning.BAD_GRAMMAR, 1) }

    private val processor = Processor(listOf(LanguageToolModule()), listOf(mockRisk))

    private val mockAnalysisEntry =
        RiskAnalysisEntry("Incorrect Grammar", "email is not properly written", RiskLevel.C)

    val rawCorrectlyWrittenEmail =
        """<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body><div dir=\"ltr\">
    Boa tarde<div><br><div>
    Este email serve como teste</div></div><div><br></div><div>
    Cumprimentos</div><div>sender</div></div></body></html>""".trim()

    val rawPoorlyWrittenContent =
        """<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body><div dir=\"ltr\">
    boa tardes<div><br><div>
    este emails serve como testes</div></div><div><br></div><div>
    Cumprimentos</div><div>sender</div></div></body></html>""".trim()

    val rawContentGibberish =
        """<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body><div dir=\"ltr\">
    Qeqasdasd adasd<div><br><div>
    Aasdas asdasd asd asdsa S</div></div><div><br></div><div>
    Adas sds a</div><div>sender</div></div></body></html>""".trim()


    @Test
    fun `LanguageToolModule test for no Threat`() {
        val email = testEmail.copy(rawBody = rawCorrectlyWrittenEmail)

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.A, eval.threat)
        Assertions.assertEquals(0, eval.threatJustification.size)
    }

    @Test
    fun `LanguageToolModule test for poorly written email`() {
        val email = testEmail.copy(rawBody = rawPoorlyWrittenContent)

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.C, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }

    @Test
    fun `LanguageToolModule test for gibberish content`() {
        val email = testEmail.copy(rawBody = rawContentGibberish)

        val eval = processor.process(listOf(email))

        Assertions.assertEquals(RiskLevel.C, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }

    @Test
    fun `LanguageToolModule test for real promotional email`() {
        val eval = processor.process(listOf(realPromotionalEmail))

        Assertions.assertEquals(RiskLevel.C, eval.threat)
        Assertions.assertEquals(1, eval.threatJustification.size)
        Assertions.assertTrue(eval.threatJustification.contains(mockAnalysisEntry))
    }
}