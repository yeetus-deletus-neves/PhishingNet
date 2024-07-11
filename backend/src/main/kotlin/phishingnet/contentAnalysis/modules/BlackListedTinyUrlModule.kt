package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog

class BlackListedTinyUrlModule: AnalysisModule {
    override val name = "Módulo URL encurtado na lista negra"

    //TODO verificar se podem ser enviados com http
    //links são sempre adicionados com https:// or http://
    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(Warning.URL_SHORTENED)

        val blackList = listOf("bit.ly", "t.co", "goo.gl")

        val urls = extractUrlsFromEmailBody(email.body)
        urls.forEach { url ->
            if(checkIfUrlIsShortened(blackList, url)) warningLog[Warning.URL_SHORTENED]++
        }
        //warningLog[Warning.URL_SHORTENED] = countShortenedLinks(email.body)

        return warningLog
    }

    private fun countShortenedLinks(text: String): Int {
        val urlPattern = "(https?://)?(bit\\.ly|tinyurl\\.com|t\\.co|goo\\.gl|buff\\.ly|is\\.gd|ow\\.ly|bitly\\.com)/[\\w-]+"
        val regex = Regex(urlPattern, RegexOption.IGNORE_CASE)
        val cnt = regex.findAll(text).count()
        println(cnt)
        return cnt
    }

    private fun extractUrlsFromEmailBody(body: String): List<String>{
        val urlPattern = "https?://[\\w-]+(\\.[\\w-]+)+(/[\\w-./?%&=]*)?"
        val regex = Regex(urlPattern)
        return regex.findAll(body).map { it.value }.toList()
    }

    private fun checkIfUrlIsShortened(blackList: List<String>, url: String): Boolean{
        blackList.forEach{ domain ->
            val regex = Regex("^https?://$domain")
            if (regex.containsMatchIn(url)) return true
        }
        return false
    }
}