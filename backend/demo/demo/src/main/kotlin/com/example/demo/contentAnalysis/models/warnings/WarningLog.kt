package com.example.demo.contentAnalysis.models.warnings

data class WarningLog (
    val warning: Warnings
){
    private var occurrences: Int = 0

    fun occurrences() = occurrences

    fun incrementOccurrences(){
        this.occurrences += 1
    }

    fun setOccurrences(occurrences: Int){
        this.occurrences = occurrences
    }
}