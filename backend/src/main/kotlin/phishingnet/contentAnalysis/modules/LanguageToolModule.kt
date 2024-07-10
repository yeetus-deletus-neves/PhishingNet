package phishingnet.contentAnalysis.modules

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import org.languagetool.JLanguageTool
import org.languagetool.language.AmericanEnglish
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

    override fun process(email: Email): WarningLog {
        val emailContent = email.body
        val warningLog = WarningLog(Warning.BAD_GRAMMAR)
        if (emailContent.isEmpty()) return warningLog
        val languages = listOf(Language.ENGLISH, Language.PORTUGUESE)
        val detector = LanguageDetectorBuilder.fromLanguages(*languages.toTypedArray()).build()
        val detectedLanguage = detector.detectLanguageOf(emailContent)

        val langTool = when(detectedLanguage.name){
            "PORTUGUESE" -> JLanguageTool(Portuguese())
            "ENGLISH" -> JLanguageTool(AmericanEnglish())
            else -> throw IllegalArgumentException("Language was not detected")
        }


        val detectedErrors = langTool.check(email.body)
        val cnt = detectedErrors.size

        //for debug purposes
        /*println(detectedLanguage)
        println("For this content: ${email.body}")
        println("Detected this errors: $detectedErrors")
        println("Number of errors: ${detectedErrors.size}")*/

        warningLog[Warning.BAD_GRAMMAR] = cnt

        return warningLog
    }
}