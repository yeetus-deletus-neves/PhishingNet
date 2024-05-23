package phishingnet.contentAnalysis

import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.MessageHeadersInfo
import phishingnet.contentAnalysis.models.riskAnalysis.RiskAnalysis
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.Warnings
import phishingnet.contentAnalysis.modules.mock_modules.CountWordsModule
import phishingnet.contentAnalysis.modules.mock_modules.HardCodedWordCounter
import phishingnet.contentAnalysis.modules.mock_modules.MyNameWasMentionedModule

fun main() {


    //Setting modules do be used
    val moduleList = listOf(
        CountWordsModule(),
        MyNameWasMentionedModule(),
        HardCodedWordCounter()
    )

    //Setting up a registered risk
    val testRisk = Risk(
        "Test Risk",
        "My name was mentioned in a text of considerable length.",
        RiskLevel.SUSPICIOUS
    )
    testRisk.setRequirement(Warnings.WORD_COUNTED, 5)
    testRisk.setRequirement(Warnings.NAME_MENTIONED)

    //-------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------
    val text = "Olá Manuel, vimos por este meio informar-lo que possui uma encomenda presa nos correios que precisa de ser resgatada. Por favor utilize o seguinte link:"
    val email = Email(text, MessageHeadersInfo("<abc>","abc", "abc"))

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