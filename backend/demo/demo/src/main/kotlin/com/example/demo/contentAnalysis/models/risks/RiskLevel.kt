package com.example.demo.contentAnalysis.models.risks

enum class RiskLevel(val level: Int) {
    NoThreat(0),
    ShouldLookIntoIt(1),
    Suspicious(2),
    VerySuspicious(3),
    Alarming(4);

    override fun toString(): String {
        val parts = StringBuilder()
        var startIdx = 0
        for (i in 1 until name.length) {
            if (name[i].isUpperCase()) {
                parts.append(name.substring(startIdx, i)).append(" ")
                startIdx = i
            }
        }
        return parts.append(name.substring(startIdx)).toString()
    }
}
