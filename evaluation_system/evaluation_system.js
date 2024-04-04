
const threatLevels = {
    NoThreat: 0,
    ShouldLookIntoIt: 1,
    Suspicious: 2,
    VerySuspicious: 3,
    Alarming: 4
}

/*
asked For IBAN and is the first time sending and email VerySuspicious
asked For IBAN and similar email to another existing one VerySuspicious
*/

const warnings = {
    asks_for_IBAN: "asks for IBAN",
    seems_like_a_copy_of_another_email: "seems like a copy of another email",
    from_distinct_from_return_path: "from distinct from return path",
    DMARC_auth_failed: "DMARC auth failed",
    spelling_mistakes_and_bad_grammar: "spelling mistakes and bad grammar",
    malicious_url: "malicious url",
    malicious_attachment: "malicious attachment"
}

const attacks = {
    false_entity: "false entity",
    malicious_software: "malicious software",
    ilegitimate_IBAN: "ilegitimate IBAN",
    CEO_fraud: "CEO fraud",
    email_spoofing: "email spoofing",
    business_email_compromise: "business email compromise"

}

let eval = [
    {
        warning: warnings.asks_for_IBAN, 
        eval: ShouldLookIntoIt,
        possible_attacks: [attacks.ilegitimate_IBAN]
    },
    {
        warning: warnings.seems_like_a_copy_of_another_email, 
        eval: Suspicious,
        possible_attacks: [attacks.false_entity, attacks.ilegitimate_IBAN]
    },
    {
        warning: warnings.from_distinct_from_return_path, 
        eval: Alarming,
        possible_attacks: [attacks.false_entity, attacks.malicious_software, attacks.ilegitimate_IBAN, attacks.email_spoofing]
    },
    {
        warning: warnings.DMARC_auth_failed, 
        eval: Alarming,
        possible_attacks: [attacks.false_entity, attacks.malicious_software, attacks.ilegitimate_IBAN,]
    },
    {
        warning: warnings.spelling_mistakes_and_bad_grammar, 
        eval: ShouldLookIntoIt,
        possible_attacks: [attacks.false_entity, attacks.ilegitimate_IBAN]
    },
    {
        warning: warnings.malicious_url, 
        eval: Alarming,
        possible_attacks: [attacks.false_entity, attacks.malicious_software, attacks.CEO_fraud, attacks.business_email_compromise]
    },
    {
        warning: warnings.malicious_attachment, 
        eval: Alarming,
        possible_attacks: [attacks.false_entity, attacks.malicious_software, attacks.CEO_fraud, attacks.business_email_compromise]
    },
]