package com.example.demo.contentAnalysis.evaluation.models

enum class ThreatLevel(val level: Int) {
    ShouldLookIntoIt(0),
    Suspicious(1),
    VerySuspicious(2),
    Alarming(3)
}
