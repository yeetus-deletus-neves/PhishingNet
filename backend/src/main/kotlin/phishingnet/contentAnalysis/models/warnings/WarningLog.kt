package phishingnet.contentAnalysis.models.warnings

import kotlin.collections.getOrPut


data class WarningLog(
    val warnings: MutableMap<Warning, Occurrences>
) {
    constructor(warningList: List<Warning>) : this(
        mutableMapOf<Warning, Occurrences>().apply {
            warningList.forEach { warning ->
                getOrPut(warning) { Occurrences() }
            }
        }
    )

    constructor(warning: Warning) : this(
        mutableMapOf<Warning, Occurrences>().apply {
            getOrPut(warning) { Occurrences() }
        }
    )

    operator fun get(warning: Warning): Occurrences = warnings[warning] ?: throw IllegalArgumentException()
    operator fun set(warning: Warning, value: Occurrences) {
        warnings[warning] = value
    }
}


data class Occurrences(private var occurrences: Int = 0) {

    fun get() = occurrences

    fun incrementOccurrences() {
        this.occurrences += 1
    }

    fun setOccurrences(occurrences: Int) {
        this.occurrences = occurrences
    }

}

fun Int.toOccurrences(): Occurrences = Occurrences(this)
