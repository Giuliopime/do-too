package app.dotoo.config.core

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.util.reflect.*
import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf

class ConfigurationManager(
    private val packageName: String,
    private val configurationReader: (key: String, clazz: KClass<*>) -> Any?,
) {
    companion object {
        private val log = KotlinLogging.logger { }

        const val DEFAULT_CONFIG_PACKAGE = "app.dotoo.config"
    }

    @Suppress("UNUSED")
    fun listConfigurations(): List<ConfigurationData> {
        val configurations = mutableListOf<ConfigurationData>()

        // Read all objects via reflection
        val reflections = Reflections(packageName)

        val configObjects = reflections.getTypesAnnotatedWith(Configuration::class.java)
        log.debug { "Found ${configObjects.size} configuration objects" }

        configObjects.forEach { configObject ->
            // Make sure each reflection result is an actual Kotlin Object
            val obj = configObject.kotlin
            val configAnnotation =
                obj.findAnnotation<Configuration>()
                    ?: throw IllegalArgumentException("Missing @Configuration annotation on configuration object: ${configObject.name}")

            if (obj.objectInstance == null) {
                throw IllegalArgumentException("Found class annotated with @Configuration while not being an Object: ${configObject.name}")
            }

            obj.declaredMemberProperties
                .filter {
                    // Filter properties with annotation
                    if (it.hasAnnotation<ConfigurationProperty>()) {
                        true
                    } else {
                        log.warn {
                            "Found a property inside a @Configuration object without the @ConfigurationProperty annotation: ${configObject.name + "." + it.name}"
                        }
                        false
                    }
                }.map {
                    // Make sure each property is mutable
                    if (!it.instanceOf(KMutableProperty::class)) {
                        throw IllegalArgumentException(
                            "Found an immutable property annotated with @ConfigurationProperty: ${configObject.name + "." + it.name}",
                        )
                    }

                    it as KMutableProperty<*>
                }
                .forEach propertyForEach@{ configProperty ->
                    val annotation =
                        configProperty.findAnnotation<ConfigurationProperty>()
                            ?: throw IllegalArgumentException("Found a property inside a @Configuration object without the @ConfigurationProperty annotation: ${configObject.name + "." + configProperty.name}")

                    // Get the full key of the configuration property
                    val key = formatKeySeparators(configAnnotation.prefix + "." + annotation.name).uppercase()
                    // Get the type
                    val clazz =
                        configProperty.returnType.classifier?.let { it as KClass<*> }
                            ?: throw IllegalArgumentException("Unsupported type for ${configObject.name + "." + configProperty.name}")

                    val defaultValue =
                        try {
                            configProperty.getter.call(obj.objectInstance)
                        } catch (e: Exception) {
                            null
                        }

                    configurations.add(
                        ConfigurationData(
                            key = key,
                            defaultValue = defaultValue,
                            type = clazz.simpleName ?: "Unknown type",
                        ),
                    )
                }
        }

        return configurations
    }

    /**
     * Initializes all the properties annotated with [ConfigurationProperty] of objects annotated with [Configuration]
     *
     * Uses the [configurationReader] to read the values for the properties
     */
    fun initialize() {
        log.debug { "Initializing configuration properties" }

        // Read all objects via reflection
        val reflections = Reflections(packageName)

        val configObjects = reflections.getTypesAnnotatedWith(Configuration::class.java)
        log.debug { "Found ${configObjects.size} configuration objects" }

        configObjects.forEach { configObject ->
            // Make sure each reflection result is an actual Kotlin Object
            val obj = configObject.kotlin
            val configAnnotation =
                obj.findAnnotation<Configuration>()
                    ?: throw IllegalArgumentException("Missing @Configuration annotation on configuration object: ${configObject.name}")

            if (obj.objectInstance == null) {
                throw IllegalArgumentException("Found class annotated with @Configuration while not being an Object: ${configObject.name}")
            }

            obj.declaredMemberProperties
                .filter {
                    // Filter properties with annotation
                    if (it.hasAnnotation<ConfigurationProperty>()) {
                        true
                    } else {
                        log.warn {
                            "Found a property inside a @Configuration object without the @ConfigurationProperty annotation: ${configObject.name + "." + it.name}"
                        }
                        false
                    }
                }.map {
                    // Make sure each property is mutable
                    if (!it.instanceOf(KMutableProperty::class)) {
                        throw IllegalArgumentException(
                            "Found an immutable property annotated with @ConfigurationProperty: ${configObject.name + "." + it.name}",
                        )
                    }

                    it as KMutableProperty<*>
                }
                .forEach propertyForEach@{ configProperty ->
                    val annotation =
                        configProperty.findAnnotation<ConfigurationProperty>()
                            ?: throw IllegalArgumentException("Found a property inside a @Configuration object without the @ConfigurationProperty annotation: ${configObject.name + "." + configProperty.name}")

                    // Get the full key of the configuration property
                    val key = formatKeySeparators(configAnnotation.prefix + "." + annotation.name).uppercase()
                    // Get the type
                    val clazz =
                        configProperty.returnType.classifier?.let { it as KClass<*> }
                            ?: throw IllegalArgumentException("Unsupported type for ${configObject.name + "." + configProperty.name}")

                    // Read the value with the user provided function
                    val value =
                        configurationReader(key, clazz)
                            ?: try {
                                configProperty.getter.call(obj.objectInstance).also {
                                    log.debug { "Using default value for key '$key': $this" }
                                }
                            } catch (_: Exception) {
                                null
                            }

                    // Null check
                    if (value == null && !configProperty.returnType.isMarkedNullable) {
                        if (annotation.optional) {
                            return@propertyForEach
                        } else {
                            throw IllegalArgumentException(
                                "Missing value for key '$key'\nFound a Null value for key '$key' while the related @ConfigurationProperty doesn't allow Null",
                            )
                        }
                    }

                    // Type check
                    if (value != null && !value.javaClass.kotlin.isSubclassOf(clazz)) {
                        throw IllegalArgumentException(
                            "Wrong type for key '$key'\nExpected a $clazz but received a ${value.javaClass.kotlin}",
                        )
                    }

                    // Set value
                    configProperty.setter.call(obj.objectInstance, value)
                }
        }

        log.debug { "Configuration properties loaded" }
    }

    private fun formatKeySeparators(key: String) = key.replace(".", "_")

    data class ConfigurationData(
        val key: String,
        val defaultValue: Any?,
        val type: String,
    ) {
        override fun toString(): String {
            return "${if (type != "String") "# $type\n" else "" }$key=${defaultValue ?: ""}"
        }
    }
}
