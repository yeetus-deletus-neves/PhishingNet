package phishingnet.contentAnalysis.models.warnings

enum class Warnings(description: String){
    ASKS_FOR_IBAN("asks for IBAN"),
    SEEMS_LIKE_A_COPY_OF_ANOTHER_EMAIL("seems like a copy of another email"),
    FROM_DISTINCT_FROM_RETURN_PATH("from distinct from return path"),
    DMARC_AUTH_FAILED("DMARC auth failed"),
    SPELLING_MISTAKES_AND_BAD_GRAMMAR("spelling mistakes and bad grammar"),
    MALICIOUS_URL("malicious url"),
    MALICIOUS_ATTACHMENT("malicious attachment"),

    //MOCK WARNINGS
    WORD_COUNTED("We counted words."),
    NAME_MENTIONED("My name was mentioned."),
}