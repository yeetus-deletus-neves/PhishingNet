package phishingnet.contentAnalysis.modules

class HeaderModuleTest {

    /*
    @Test
    fun `HeaderModule test for no Threat`(){
        val email = Email("", MessageHeadersInfo("\"1\" <email1>", "email1", "sec"))
        val headerModule = HeaderModule()
        val process = Processor(listOf(headerModule))
        val eval = process.process(email)
        Assertions.assertEquals(1, eval.size)
        Assertions.assertTrue(eval.containsKey(Risk.FALSE_ENTITY))
        Assertions.assertEquals(ThreatLevel.NoThreat, eval[Risk.FALSE_ENTITY]!!.threatLevel)
        Assertions.assertEquals(headerModule.name, eval[Risk.FALSE_ENTITY]!!.moduleOfOrigin)
    }

    @Test
    fun `HeaderModule test for different return path and from`(){
        val email = Email("", MessageHeadersInfo("\"1\" <email1>", "email2", "sec"))
        val headerModule = HeaderModule()
        val process = Processor(listOf(headerModule))
        val eval = process.process(email)
        Assertions.assertEquals(1, eval.size)
        Assertions.assertTrue(eval.containsKey(Risk.FALSE_ENTITY))
        Assertions.assertEquals(ThreatLevel.Suspicious, eval[Risk.FALSE_ENTITY]!!.threatLevel)
        Assertions.assertEquals(headerModule.name, eval[Risk.FALSE_ENTITY]!!.moduleOfOrigin)
    }
     */
}