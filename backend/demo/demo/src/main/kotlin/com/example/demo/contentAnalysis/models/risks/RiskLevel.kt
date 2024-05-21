package com.example.demo.contentAnalysis.models.risks

enum class RiskLevel(val level: Int) {
    NoThreat(0),
    ShouldLookIntoIt(1),
    Suspicious(2),
    VerySuspicious(3),
    Alarming(4)
}
