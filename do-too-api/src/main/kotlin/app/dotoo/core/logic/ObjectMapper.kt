package app.dotoo.core.logic

import app.dotoo.core.logic.typedId.serialization.IdKotlinXSerializationModule
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory

@Factory
class ObjectMapper {
    val json = Json {
        serializersModule = IdKotlinXSerializationModule
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    inline fun <reified T> encode(data: T): String {
        return json.encodeToString(data)
    }

    inline fun <reified T> decode(serializedData: String): T {
        return json.decodeFromString(serializedData)
    }
}
