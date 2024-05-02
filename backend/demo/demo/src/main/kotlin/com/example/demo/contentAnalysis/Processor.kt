package com.example.demo.contentAnalysis

import com.example.demo.contentAnalysis.models.ProcessorConfig
import com.example.demo.contentAnalysis.models.Risk

class Processor (private val config: ProcessorConfig){

    fun process(content: String): List<Risk>{
        TODO()
    }

    private fun cleanContent(content: String): String{
        TODO()
    }
}