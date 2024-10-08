package app.dotoo.core.logic.typedId.serialization

import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.core.logic.typedId.newDtId
import app.dotoo.core.logic.typedId.newDtIntId
import app.dotoo.data.models.email.EmailVerificationData
import app.dotoo.data.models.user.UserData
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IdKotlinXSerializationModuleKtTest {
    private val json =
        Json {
            serializersModule = IdKotlinXSerializationModule
            prettyPrint = true
        }

    @Test
    fun idSerialization() {
        val id = newDtId<UserData>()
        val serialized =
            json.encodeToString(id).let {
                it.substring(1, it.length - 1)
            }

        assertEquals(id.toString(), serialized)

        val intId = newDtIntId<EmailVerificationData>()
        val serializedInt =
            json.encodeToString(intId).let {
                it.substring(1, it.length - 1)
            }

        assertEquals(intId.toString(), serializedInt)
    }

    @Test
    fun objectWithIdSerialization() {
        @Serializable
        data class TestObject(
            @Contextual val id: DtId<UserData>,
            val name: String,
        )

        val testObject =
            TestObject(
                id = newDtId(),
                name = "test",
            )
        val serialized = json.encodeToString(testObject)

        assertEquals(
            """
            {
                "id": "${testObject.id}",
                "name": "${testObject.name}"
            }
            """.trimIndent(),
            serialized,
        )
    }
}
