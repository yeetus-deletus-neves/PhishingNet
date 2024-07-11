package phishingnet.contentAnalysis.models.warnings

enum class Warning(description: String){
    ASKS_FOR_IBAN("Pede IBAN"),
    SEEMS_LIKE_A_COPY_OF_ANOTHER_EMAIL("Parece uma cópia de outro email"),
    FROM_DISTINCT_RETURN_PATH("Remetente diferente do caminho de retorno"),
    HEADER_CERTIFICATES_AUTH_FAILED("Falha na autenticação dos certificados de cabeçalho"),
    DMARC_AUTH_FAILED("Autorização DMARC falhou"),
    DKIM_AUTH_FAILED("Autorização DKIM falhou"),
    SPF_AUTH_FAILED("Autorização SPF falhou"),
    SPELLING_MISTAKES_AND_BAD_GRAMMAR("Erros ortográficos e má gramática"),
    MALICIOUS_URL("url malicioso"),
    BAD_GRAMMAR("Má gramática"),
    PAST_EMAILS_SENT("Número de emails trocados anteriormente com este endereço"),
    FILE_ATTACHED_CAN_BE_DANGEROUS("Pelo menos um dos ficheiros anexados ao email é um executável"),
    URGENCY("Email marcado como urgente"),
    URL_SHORTENED("Url foi encurtado"),

    //WARNINGS para efeitos de teste
    WORD_COUNTED("Contagem de palavras"),
    NAME_MENTIONED("Nome foi mencionado"),

    //WARNINGS experimentais
    LLM_TRIGGERED("LLM detetou um elevado nivel de possibilidade de se tratar de um email de phishing")
}