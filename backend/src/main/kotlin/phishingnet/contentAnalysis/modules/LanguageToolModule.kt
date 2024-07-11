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
 * Deteta erros ortográficos e gramaticais
 *
 * Por vezes retorna apenas 1 erro mesmo que mais sejam detetáveis e
 * por vezes deteta erros que não nos interessam,
 * pelo que devemos usá-lo com algum cuidado e limitar a importância na análise
 *
 */

class LanguageToolModule: AnalysisModule {
    override val name: String = "Módulo Language Tool"

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
            else -> throw IllegalArgumentException("Linguagem não foi detetada")
        }


        val detectedErrors = langTool.check(email.body)
        val cnt = detectedErrors.size

        warningLog[Warning.BAD_GRAMMAR] = cnt

        return warningLog
    }
}