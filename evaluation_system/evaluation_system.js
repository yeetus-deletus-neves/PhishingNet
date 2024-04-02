
const threatLevels = {
    NoThreat: 0,
    ShouldLookIntoIt: 1,
    Suspicious: 2,
    VerySuspicious: 3,
    Alarming, 4
}

let eval = [
    {
        name: "asked for IBAN", 
        eval: ShouldLookIntoIt
    },
    {
        name: "asked for IBAN and is the first time sending and email", 
        eval: VerySuspicious
    },
    {
        name: "asked for IBAN and similar email to another existing one", 
        eval: VerySuspicious
    },
    {
        name: "email very similar to another one with already exchanged emails", 
        eval: Suspicious
    },
    {
        name: "from vs return path", 
        eval: Alarming
    },
    {
        name: "DMARC auth failed", 
        eval: Alarming
    },
    {
        name: "spelling mistakes and bad grammar", 
        eval: ShouldLookIntoIt
    },
    {
        name: "malicious url", 
        eval: Alarming
    },
    {
        name: "malicious attachment", 
        eval: Alarming
    },
    {
        name: "asked for IBAN", 
        eval: ShouldLookIntoIt
    },

]