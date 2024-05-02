package com.example.demo.contentAnalysis.test

enum class Risk(val description: String) {
    FALSE_ENTITY("false entity"),
    MALICIOUS_SOFTWARE("malicious software"),
    ILLEGITIMATE_IBAN("illegitimate IBAN"),
    CEO_FRAUD("CEO fraud"),
    EMAIL_SPOOFING("email spoofing"),
    BUSINESS_EMAIL_COMPROMISE("business email compromise");

    companion object {
        val allRisks: List<Risk> = entries
    }
}
