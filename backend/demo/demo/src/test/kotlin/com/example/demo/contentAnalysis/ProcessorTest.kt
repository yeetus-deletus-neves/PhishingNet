package com.example.demo.contentAnalysis

import com.example.demo.contentAnalysis.models.Email
import com.example.demo.contentAnalysis.models.Risk
import com.example.demo.contentAnalysis.models.ThreatLevel
import com.example.demo.contentAnalysis.modules.CountWordsModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class ProcessorTest {

    @Test
    fun `CountWordsModule test for no Threat`(){
        val email = Email("1word", null)
        val cntModule = CountWordsModule()
        val process = Processor(listOf(cntModule))
        val eval = process.process(email)
        assertEquals(1, eval.size)
        assertTrue(eval.containsKey(Risk.MOCK_RISK))
        assertEquals(ThreatLevel.NoThreat, eval[Risk.MOCK_RISK]!!.threatLevel)
        assertEquals(cntModule.name, eval[Risk.MOCK_RISK]!!.moduleOfOrigin)
    }

    @Test
    fun `CountWordsModule test for less than 5 words`(){
        val email = Email("1word 2word 3word 4word 5word 6word", null)
        val cntModule = CountWordsModule()
        val process = Processor(listOf(cntModule))
        val eval = process.process(email)
        assertEquals(1, eval.size)
        assertTrue(eval.containsKey(Risk.MOCK_RISK))
        assertEquals(ThreatLevel.Suspicious, eval[Risk.MOCK_RISK]!!.threatLevel)
        assertEquals(cntModule.name, eval[Risk.MOCK_RISK]!!.moduleOfOrigin)
    }

    @Test
    fun `CountWordsModule test for more than 10 words`(){
        val email = Email("1w 2w 3w 4w 5w 6w 7w 8w 9w 10w 11w", null)
        val cntModule = CountWordsModule()
        val process = Processor(listOf(cntModule))
        val eval = process.process(email)
        assertEquals(1, eval.size)
        assertTrue(eval.containsKey(Risk.MOCK_RISK))
        assertEquals(ThreatLevel.VerySuspicious, eval[Risk.MOCK_RISK]!!.threatLevel)
        assertEquals(cntModule.name, eval[Risk.MOCK_RISK]!!.moduleOfOrigin)
    }

}