package com.example.demo.services.analysisServices

//nullable just for now
class Processor(private val modules: List<AnalysisModule>?) {

    //change content type
    fun process(content: String): List<RiskAnalysis> {
        //take this riskCriteria outside and hard code it
        val riskCriteria: RiskCriteria = RiskCriteria(Warning.FROM_DISTINCT_FROM_RETURN_PATH, ThreatLevel.Alarming, Risk.allRisks)
        val criteria: List<RiskCriteria> = listOf(riskCriteria)

        val evaluator = RiskEvaluator(criteria)

        return listOf(evaluator.evaluateRisk(Risk.allRisks))
    }

    private fun cleanContent(content: String) {
        TODO()
    }

}