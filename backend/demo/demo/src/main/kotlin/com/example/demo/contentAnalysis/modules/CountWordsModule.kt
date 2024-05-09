package com.example.demo.contentAnalysis.modules


import com.example.demo.contentAnalysis.AnalysisModule
import com.example.demo.contentAnalysis.models.*


class CountWordsModule: AnalysisModule {
    override val name: String = "Count Words Module"
    override var active: Boolean = false
    override fun process(email: Email): RiskAnalysis {
        val cnt = countWords(email.cleanContent)
        val analysis = evaluate(cnt)

        return analysis
    }

    private fun evaluate(cnt: Int): RiskAnalysis {
        val analysis = mutableMapOf<Risk, ThreatLevel>()

        analysis[Risk.MOCK_RISK] = if(cnt <= 5) ThreatLevel.NoThreat
        else if (cnt <= 10) ThreatLevel.Suspicious
        else ThreatLevel.VerySuspicious
        return analysis
    }


    private fun countWords(str: String): Int {
        val punctuation = setOf('.', ',', '!', '?', ';', ':', '"', '\'')
        val cleanStr = str.filterNot { it in punctuation }

        val words = cleanStr.split(" ").filter { it.isNotBlank() }

        return words.size
    }

}