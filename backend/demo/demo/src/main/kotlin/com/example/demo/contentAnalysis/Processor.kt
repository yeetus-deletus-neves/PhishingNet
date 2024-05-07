package com.example.demo.contentAnalysis

import com.example.demo.contentAnalysis.models.*

//nullable just for now
class Processor(private val modules: List<AnalysisModule>?) {

    //change content type
    fun process(email: Email): List<RiskAnalysis> {
        require (modules != null){"No existent modules to evaluate email"}
        for (module in modules) {
            module.process(email)
        }

        //take this riskCriteria outside and hard code it
        val riskCriteria: RiskCriteria = RiskCriteria("ex1", 2, ThreatLevel.Alarming)
        val criteria: List<RiskCriteria> = listOf(riskCriteria)

        val evaluator = RiskEvaluator(criteria)

        return listOf(evaluator.evaluate(Risk.allRisks))
    }
}