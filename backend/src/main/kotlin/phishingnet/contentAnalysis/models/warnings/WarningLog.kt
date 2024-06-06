package phishingnet.contentAnalysis.models.warnings


class WarningLog{
    val warnings: MutableMap<Warning, Int> = mutableMapOf()

    constructor(warningList: List<Warning>){
        for (warning in warningList){
            this.warnings[warning] = 0
        }
    }

    constructor(warning: Warning){
        this.warnings[warning] = 0
    }

    operator fun get(warning: Warning): Int = warnings[warning] ?: throw IllegalArgumentException()
    operator fun set(warning: Warning, value: Int) {
        warnings[warning] = value
    }
    fun incrementOccurrences(warning: Warning) {
        warnings[warning] = get(warning)+1
    }
}

