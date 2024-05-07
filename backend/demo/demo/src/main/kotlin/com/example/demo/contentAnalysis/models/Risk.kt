package com.example.demo.contentAnalysis.models

enum class Risk(val description: String) {
    MOCK_RISK("mock risk, for testing purposes only"),

    FALSE_ENTITY("false entity"),
    MALICIOUS_SOFTWARE("malicious software"),
    ILLEGITIMATE_IBAN("illegitimate IBAN"),
    CEO_FRAUD("CEO fraud"),
    EMAIL_SPOOFING("email spoofing"),
    BUSINESS_EMAIL_COMPROMISE("business email compromise");

    companion object {
        val allRisks: List<Risk> = entries.drop(MOCK_RISK.ordinal)
    }
}