package com.example.demo.contentAnalysis.evaluation.models

import com.example.demo.contentAnalysis.models.Risk

data class Threat (
    val name: String,
    // Risk and minimum number of occurrences
    val riskRequirements: Map<Risk,Int>,
    val level: ThreatLevel
)


