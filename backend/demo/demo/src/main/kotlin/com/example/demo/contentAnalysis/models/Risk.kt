package com.example.demo.contentAnalysis.models

typealias RiskAnalysis = Map<Risk, ThreatLevel>

enum class Risk(val description: String, severity: Severity) {
    MOCK_RISK("mock risk, for testing purposes only", Severity.NONE),

    FALSE_ENTITY("false entity", Severity.MAJOR),
    MALICIOUS_SOFTWARE("malicious software", Severity.CRITICAL),
    ILLEGITIMATE_IBAN("illegitimate IBAN", Severity.CRITICAL),
    CEO_FRAUD("CEO fraud", Severity.MAJOR),
    EMAIL_SPOOFING("email spoofing", Severity.MAJOR),
    BUSINESS_EMAIL_COMPROMISE("business email compromise", Severity.MAJOR);

    companion object {
        val allRisks: List<Risk> = entries.drop(MOCK_RISK.ordinal)
    }
}

data class RiskCriteria (
    val name: String,
    val minValue: Int,
    val threatLevel: ThreatLevel
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

enum class Severity() {
    CRITICAL,
    MAJOR,
    MINOR,
    NONE

}