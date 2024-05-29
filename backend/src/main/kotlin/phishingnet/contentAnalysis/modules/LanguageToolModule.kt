package phishingnet.contentAnalysis.modules

import org.languagetool.JLanguageTool
import org.languagetool.language.Portuguese
import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog

/***
 * Detects spelling and grammatical errors
 *
 *
 * The way it works a lot of times it just returns 1 error, even if more are detectable,
 * so we must only use it as a binary evaluation based on it either as errors or it does not,
 * not considering the amount errors for the evaluation
 *
 * This implementation does not consider gibberish words as grammatical errors
 * */

class LanguageToolModule: AnalysisModule {
    override val name: String = "Language Tool"
    override var active: Boolean = false

    override fun process(email: Email): WarningLog {
        val langTool = JLanguageTool(Portuguese())
        //val langTool = JLanguageTool(AmericanEnglish())

        val warningLog = WarningLog(Warning.BAD_GRAMMAR)

        val detectedErrors = langTool.check(email.body)
        val cnt = detectedErrors.size

        //for debug purposes
        println("For this content: ${email.body}")
        println("Detected this errors: $detectedErrors")
        println("Number of errors: ${detectedErrors.size}")

        warningLog[Warning.BAD_GRAMMAR].setOccurrences(cnt)

        return warningLog
    }
}