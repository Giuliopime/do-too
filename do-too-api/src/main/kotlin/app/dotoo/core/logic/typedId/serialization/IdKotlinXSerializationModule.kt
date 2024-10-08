package app.dotoo.core.logic.typedId.serialization

import app.dotoo.core.logic.typedId.Id
import app.dotoo.core.logic.typedId.IdGenerator
import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.core.logic.typedId.impl.DtIdGenerator
import app.dotoo.core.logic.typedId.impl.DtIntId
import app.dotoo.core.logic.typedId.impl.DtIntIdGenerator
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlin.reflect.KClass

/**
 * The Id kotlin.x Serialization module.
 */
val IdKotlinXSerializationModule: SerializersModule by lazy {
    SerializersModule {
        contextual(Id::class, IdSerializer())
        contextual(DtId::class, DtIdSerializer())
        contextual(DtIntId::class, DtIntIdSerializer())
        if (IdGenerator.defaultGenerator.idClass != DtId::class &&
            IdGenerator.defaultGenerator.idClass != DtId::class
        ) {
            @Suppress("UNCHECKED_CAST")
            contextual(
                IdGenerator.defaultGenerator.idClass as KClass<Id<*>>,
                IdSerializer(),
            )
        }
    }
}

private class IdSerializer<T : Id<*>> : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("IdSerializer", PrimitiveKind.STRING)

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): T = IdGenerator.defaultGenerator.create(decoder.decodeString()) as T

    override fun serialize(
        encoder: Encoder,
        value: T,
    ) {
        encoder.encodeString(value.toString())
    }
}

private class DtIdSerializer<T : DtId<*>> : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("IdSerializer", PrimitiveKind.STRING)

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): T = DtIdGenerator().create(decoder.decodeString()) as T

    override fun serialize(
        encoder: Encoder,
        value: T,
    ) {
        encoder.encodeString(value.toString())
    }
}

private class DtIntIdSerializer<T : DtIntId<*>> : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("IdSerializer", PrimitiveKind.STRING)

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): T = DtIntIdGenerator().create(decoder.decodeString()) as T

    override fun serialize(
        encoder: Encoder,
        value: T,
    ) {
        encoder.encodeString(value.toString())
    }
}
