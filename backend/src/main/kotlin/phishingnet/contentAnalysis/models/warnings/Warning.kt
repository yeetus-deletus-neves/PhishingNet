package phishingnet.contentAnalysis.models.warnings

enum class Warning(description: String){
    ASKS_FOR_IBAN("asks for IBAN"),
    SEEMS_LIKE_A_COPY_OF_ANOTHER_EMAIL("seems like a copy of another email"),
    FROM_DISTINCT_FROM_RETURN_PATH("from distinct from return path"),
    HEADER_CERTIFICATES_AUTH_FAILED("Header certificates auth failed"),
    DMARC_AUTH_FAILED("DMARC auth failed"),
    DKIM_AUTH_FAILED("DKIM auth failed"),
    SPF_AUTH_FAILED("SPF auth failed"),
    SPELLING_MISTAKES_AND_BAD_GRAMMAR("spelling mistakes and bad grammar"),
    MALICIOUS_URL("malicious url"),
    MALICIOUS_ATTACHMENT("malicious attachment"),
    BAD_GRAMMAR("bad grammar"),
    ASKS_FOR_SENSITIVE_INFO("bad grammar"),

    //MOCK WARNINGS
    WORD_COUNTED("We counted words."),
    NAME_MENTIONED("My name was mentioned."),
}