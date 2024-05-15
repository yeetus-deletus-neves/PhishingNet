package com.example.demo.contentAnalysis.evaluation.models.risks

enum class Risks(description: String){
    GRAMMATICAL_ERROR("GRAMMATICAL_ERROR"),
    SUSPICIOUS_HEADERS("MODIFIER_ERROR"),
    CONTAINS_IBAN("CONTAINS_IBAN"),
    SUSPICIOUS_LINK("SUSPICIOUS_LINK"),
}