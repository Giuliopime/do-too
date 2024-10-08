package app.dotoo.data.models.auth

import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.data.models.user.UserData
import io.ktor.server.auth.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthSessionData(
    @Contextual val id: DtId<UserAuthSessionData>,
    @Contextual val userId: DtId<UserData>,
    val iat: Long,
    val deviceName: String?,
    val ip: String,
) : Principal
