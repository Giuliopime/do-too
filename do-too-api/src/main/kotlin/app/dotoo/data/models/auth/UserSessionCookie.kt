package app.dotoo.data.models.auth

import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.data.models.user.UserData
import io.ktor.server.auth.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Content of the auth-user-session cookie
 */
@Serializable
data class UserSessionCookie(
    @Contextual val session_id: DtId<UserAuthSessionData>,
    @Contextual val user_id: DtId<UserData>,
) : Principal
