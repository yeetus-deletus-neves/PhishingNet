package com.example.demo.contentAnalysis.modules

import com.example.demo.contentAnalysis.Processor
import com.example.demo.contentAnalysis.models.Email
import com.example.demo.contentAnalysis.models.MessageHeadersInfo
import com.example.demo.contentAnalysis.models.Risk
import com.example.demo.contentAnalysis.models.ThreatLevel
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HeaderModuleTest {

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
}