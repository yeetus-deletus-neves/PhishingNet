package com.example.demo.contentAnalysis.evaluation.models.risks

import com.example.demo.contentAnalysis.models.Risk

data class RiskFound (
    val risk: Risk
){
    private var occurrences: Int = 1

    fun occurrences() = occurrences

    fun incrementOccurrences(){
        occurrences += 1
    }
}