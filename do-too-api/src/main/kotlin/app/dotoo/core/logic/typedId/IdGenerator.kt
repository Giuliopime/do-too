package app.dotoo.core.logic.typedId

import app.dotoo.core.logic.typedId.impl.DtIdGenerator
import kotlin.reflect.KClass
import kotlin.reflect.full.valueParameters

/**
 * A generator of Ids.
 */
interface IdGenerator {
    companion object {
        var defaultGenerator: IdGenerator
            get() = defaultIdGenerator
            set(value) {
                defaultIdGenerator = value
            }

        @Volatile
        private var defaultIdGenerator: IdGenerator = DtIdGenerator()
    }

    /**
     * The class of the id.
     */
    val idClass: KClass<out Id<*>>

    /**
     * The class of the wrapped id.
     */
    val wrappedIdClass: KClass<out Any>

    /**
     * Generate a new id.
     */
    fun <T> generateNewId(): Id<T>

    /**
     * Create a new id from its String representation.
     */
    fun create(s: String): Id<*> =
        idClass
            .constructors
            .firstOrNull { it.valueParameters.size == 1 && it.valueParameters.first().type.classifier == String::class }
            ?.call(s)
            ?: error("no constructor with a single string arg found for $idClass}")
}
