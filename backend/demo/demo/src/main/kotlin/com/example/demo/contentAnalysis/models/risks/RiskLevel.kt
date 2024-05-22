package com.example.demo.contentAnalysis.models.risks

enum class RiskLevel(val level: Int) {
    NO_THREAT(0),
    SHOULD_LOOK_INTO_IT(1),
    SUSPICIOUS(2),
    VERY_SUSPICIOUS(3),
    ALARMING(4);

    override fun toString(): String {
        val words = name.split('_')
        return words.joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
    }
}
