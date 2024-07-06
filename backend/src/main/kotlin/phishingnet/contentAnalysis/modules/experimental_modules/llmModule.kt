package phishingnet.contentAnalysis.modules.experimental_modules

import phishingnet.api.utils.HttpMethod
import phishingnet.api.utils.HttpRequest
import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog
import com.google.gson.Gson

class llmModule: AnalysisModule {
    override val name = "LLM analysis module"
    override var active = false

    private val criteria: Double = 80.0

    override fun process(email: Email): WarningLog {
        val warningLog = WarningLog(Warning.LLM_TRIGGERED)

        val evaluation = evaluate(email.body)

        if (evaluation != null && evaluation >= criteria) {
            warningLog.incrementOccurrences(Warning.LLM_TRIGGERED)
        }

        return warningLog
    }

    private fun evaluate(text: String): Double? {
        val request = HttpRequest("http://127.0.0.1:8000/prompt", HttpMethod.POST)

        request.addHeader("Accept", "*/*")
        request.addHeader("Connection", "keep-alive")
        request.addHeader("Accept-Encoding", "gzip, deflate, br")

        request.addBody("prompt", text)
        val response = request.sendRequest()
        if (response.code != 200) { return null }

        return Gson().fromJson(response.body!!.string(), EvaluationResult::class.java).phishing
    }

    data class EvaluationResult(val phishing: Double)
}