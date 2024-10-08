package app.dotoo.data.models.user

import app.dotoo.core.logic.typedId.impl.DtId
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    @Contextual val id: DtId<UserData>,
    val email: String,
    val passwordHash: String,
    val emailVerified: Boolean,
    val creationTimestamp: Long,
) {
    fun getResponseDto() = UserResponseDto(
        id = id,
        email = email,
        creation_timestamp = creationTimestamp,
    )

    @Serializable
    data class UserResponseDto(
        @Contextual val id: DtId<UserData>,
        val email: String,
        val creation_timestamp: Long,
    )
}
