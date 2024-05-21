package com.example.demo.contentAnalysis

import com.example.demo.contentAnalysis.models.Email
import com.example.demo.contentAnalysis.models.riskAnalysis.RiskAnalysis
import com.example.demo.contentAnalysis.models.risks.Risk
import com.example.demo.contentAnalysis.models.risks.RiskLevel
import com.example.demo.contentAnalysis.models.warnings.Warnings
import com.example.demo.contentAnalysis.modules.mock_modules.CountWordsModule
import com.example.demo.contentAnalysis.modules.mock_modules.MyNameWasMentionedModule

fun main() {


    //Setting modules do be used
    val moduleList = listOf(
        CountWordsModule(),
        MyNameWasMentionedModule()
    )

    //Setting up a registered risk
    val testRisk = Risk(
        "Test Risk",
        "My name was mentioned in a text of considerable length.",
        RiskLevel.Suspicious
    )
    testRisk.setRequirement(Warnings.WORD_COUNTED, 5)
    testRisk.setRequirement(Warnings.NAME_MENTIONED)

    //-------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------
    val text = "Olá Manuel, vimos por este meio informar-lo que possui uma encomenda presa nos correios que precisa de ser resgatada. Por favor utilize o seguinte link:"
    val email = Email(text, null)

    val processor = Processor(moduleList, listOf(testRisk))
    processor.process(email)

    printAnalysis(processor.process(email))


}















fun printAnalysis(analysis: RiskAnalysis){
    println("TOTAL THREAT: " + analysis.threat)
    println("RISKS FOUND: ")
    println("--------------------------------------------------------------------------------------------")
    for (risk in analysis.threatJustification){
        println("      RISK NAME: " + risk.name)
        println("      RISK DESCRIPTION: " + risk.description)
        println("      RISK THREAT: " + risk.threat)
        println("--------------------------------------------------------------------------------------------")
    }

}