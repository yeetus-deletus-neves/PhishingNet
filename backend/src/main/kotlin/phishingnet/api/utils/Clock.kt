package phishingnet.api.utils

import org.springframework.stereotype.Component
import java.time.Instant

interface Clock {
    fun now(): Instant
}
@Component
object RealClock : Clock {
    // Para ter precisão a nivel do segundo
    override fun now(): Instant = Instant.ofEpochSecond(Instant.now().epochSecond)
}