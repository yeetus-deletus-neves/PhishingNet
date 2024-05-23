package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.MessageHeadersInfo

class CountWordsModuleTest {
    private val exampleHeader = MessageHeadersInfo("\"1\" <email1>", "email2", "secStuff")

/*
    @Test
    fun `CountWordsModule test for no Threat`(){
        val email = Email("1word", exampleHeader)
        val cntModule = CountWordsModule()
        val process = Processor(listOf(cntModule))
        val eval = process.process(email)
        Assertions.assertEquals(1, eval.size)
        Assertions.assertTrue(eval.containsKey(Risk.MOCK_RISK))
        Assertions.assertEquals(ThreatLevel.NoThreat, eval[Risk.MOCK_RISK]!!.threatLevel)
        Assertions.assertEquals(cntModule.name, eval[Risk.MOCK_RISK]!!.moduleOfOrigin)
    }

    @Test
    fun `CountWordsModule test for less than 5 words`(){
        val email = Email("1word 2word 3word 4word 5word 6word", exampleHeader)
        val cntModule = CountWordsModule()
        val process = Processor(listOf(cntModule))
        val eval = process.process(email)
        Assertions.assertEquals(1, eval.size)
        Assertions.assertTrue(eval.containsKey(Risk.MOCK_RISK))
        Assertions.assertEquals(ThreatLevel.Suspicious, eval[Risk.MOCK_RISK]!!.threatLevel)
        Assertions.assertEquals(cntModule.name, eval[Risk.MOCK_RISK]!!.moduleOfOrigin)
    }

    @Test
    fun `CountWordsModule test for more than 10 words`(){
        val email = Email("1w 2w 3w 4w 5w 6w 7w 8w 9w 10w 11w", exampleHeader)
        val cntModule = CountWordsModule()
        val process = Processor(listOf(cntModule))
        val eval = process.process(email)
        Assertions.assertEquals(1, eval.size)
        Assertions.assertTrue(eval.containsKey(Risk.MOCK_RISK))
        Assertions.assertEquals(ThreatLevel.VerySuspicious, eval[Risk.MOCK_RISK]!!.threatLevel)
        Assertions.assertEquals(cntModule.name, eval[Risk.MOCK_RISK]!!.moduleOfOrigin)
    }
 */
}