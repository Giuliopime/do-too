package app.dotoo.config.core

import ch.qos.logback.classic.Level
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvException
import io.github.cdimascio.dotenv.dotenv
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.reflect.KClass

private val log = KotlinLogging.logger { }

object ConfigurationReader {
    private val dotenv: Dotenv? =
        try {
            dotenv()
        } catch (_: DotenvException) {
            log.info { ".env file not found, using System environment variables" }
            null
        }

    /**
     * Reads a value with the specified [key] from the environment, according to the [type]
     */
    fun read(key: String, type: KClass<*>, ): Any? {
        val value: String? = dotenv?.get(key) ?: System.getenv(key)

        return try {
            when (type) {
                String::class -> value
                Int::class -> value?.toInt()
                Long::class -> value?.toLong()
                Boolean::class -> value?.toBoolean()
                Level::class -> {
                    try {
                        value?.uppercase()?.let { Level.valueOf(it) }
                    } catch (_: IllegalArgumentException) {
                        throw IllegalArgumentException(
                            "Tried to read a value of type 'Level' for key '$key' but casting failed, value: $value",
                        )
                    }
                }
                else -> throw UnsupportedOperationException(
                    "Configuration reader required to read a value of type $type for key '$key' but no casting is implemented for that type",
                )
            }
        } catch (e: NumberFormatException) {
            log.error { "Could not cast a value with key '$key' in configuration reader, see following exception" }
            throw e
        }
    }
}
