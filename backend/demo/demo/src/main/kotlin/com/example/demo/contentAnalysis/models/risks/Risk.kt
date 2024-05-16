package com.example.demo.contentAnalysis.models.risks

import com.example.demo.contentAnalysis.models.warnings.Warnings

data class Risk (
    val name: String,
    val description: String,
    // Warning and minimum number of occurrences
    val level: RiskLevel
){
    val warningRequirements = mutableMapOf<Warnings,Int>()

    fun setRequirement(warnings: Warnings, minOccurrences: Int = 1) {
        warningRequirements[warnings] = minOccurrences
    }
}




