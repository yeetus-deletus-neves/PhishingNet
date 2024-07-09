package phishingnet.contentAnalysis.models.risks

enum class RiskLevel(val level: Int) {
    MOCK_RISK(0),
    A(0),
    B(1),
    C(2),
    D(3),
    E(4),
    F(4);

    /*override fun toString(): String {
        val words = name.split('_')
        return words.joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
    }*/
}
