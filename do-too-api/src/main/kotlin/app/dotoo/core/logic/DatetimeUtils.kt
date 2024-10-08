package app.dotoo.core.logic

import java.time.Instant

object DatetimeUtils {
    /**
     * Shortcut for [System.currentTimeMillis]
     */
    fun currentMillis(): Long = System.currentTimeMillis()

    fun currentJavaInstant(): Instant = Instant.now()
}
