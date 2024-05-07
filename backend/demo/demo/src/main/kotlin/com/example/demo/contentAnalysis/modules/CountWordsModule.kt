package com.example.demo.contentAnalysis.modules


import com.example.demo.contentAnalysis.AnalysisModule
import com.example.demo.contentAnalysis.models.*


class CountWordsModule: AnalysisModule {
    override val name: String = "Count Words Module"
    override var active: Boolean = false
    override fun process(email: Email): RiskAnalysis {
        val cnt = countWords(email.body)
        return if(cnt <= 25) RiskAnalysis(listOf(Risk.MOCK_RISK), ThreatLevel.NoThreat)
            else if (cnt <= 50) RiskAnalysis(listOf(Risk.MOCK_RISK), ThreatLevel.Suspicious)
            else RiskAnalysis(listOf(Risk.MOCK_RISK), ThreatLevel.VerySuspicious)
    }

    private fun countWords(str: String): Int {
        val punctuation = setOf('.', ',', '!', '?', ';', ':', '"', '\'')
        val cleanStr = str.filterNot { it in punctuation }

        val words = cleanStr.split(" ").filter { it.isNotBlank() }

        return words.size
    }

}