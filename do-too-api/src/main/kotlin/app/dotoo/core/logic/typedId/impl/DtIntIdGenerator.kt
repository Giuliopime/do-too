package app.dotoo.core.logic.typedId.impl

import app.dotoo.core.logic.typedId.Id
import app.dotoo.core.logic.typedId.IdGenerator
import com.google.errorprone.annotations.DoNotCall
import org.koin.core.annotation.Factory
import kotlin.reflect.KClass

/**
 * Generator of [DtIntId] based on [Int].
 */
@Factory
class DtIntIdGenerator : IdGenerator {
    override val idClass: KClass<out Id<*>> = DtIntId::class

    override val wrappedIdClass: KClass<out Any> = Int::class

    @DoNotCall("This doesn't generate a safe int id, it always uses 0!")
    override fun <T> generateNewId(): Id<T> = DtIntId(0)
}
