package com.example.demo.services.analysisServices

data class RiskCriteria(
    val warning: Warning,
    val threatLevel: ThreatLevel,
    val possibleAttacks: List<Risk>
)


enum class ThreatLevel(val level: Int) {
    NoThreat(0),
    ShouldLookIntoIt(1),
    Suspicious(2),
    VerySuspicious(3),
    Alarming(4)
}

enum class Warning(val description: String) {
    ASKS_FOR_IBAN("asks for IBAN"),
    SEEMS_LIKE_A_COPY_OF_ANOTHER_EMAIL("seems like a copy of another email"),
    FROM_DISTINCT_FROM_RETURN_PATH("from distinct from return path"),
    DMARC_AUTH_FAILED("DMARC auth failed"),
    SPELLING_MISTAKES_AND_BAD_GRAMMAR("spelling mistakes and bad grammar"),
    MALICIOUS_URL("malicious url"),
    MALICIOUS_ATTACHMENT("malicious attachment")
}
