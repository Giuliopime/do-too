package app.dotoo.data.daos.auth

import app.dotoo.core.logic.DatetimeUtils
import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.core.logic.typedId.newDtId
import app.dotoo.data.models.auth.UserAuthSessionData
import app.dotoo.data.models.auth.UserSessionCookie
import app.dotoo.data.models.user.UserData
import app.dotoo.data.sources.cache.cm.users.UserSessionCM
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class UserSessionDao(
    private val userSessionCM: UserSessionCM,
) {
    fun get(
        userId: DtId<UserData>,
        sessionId: DtId<UserAuthSessionData>,
    ) = userSessionCM.get(userId, sessionId)

    fun create(
        userId: DtId<UserData>,
        device: String?,
        ip: String,
    ): UserSessionCookie {
        val userSessionCookie = UserSessionCookie(newDtId(), userId)

        upsert(
            UserAuthSessionData(
                id = userSessionCookie.session_id,
                userId = userId,
                iat = DatetimeUtils.currentMillis(),
                deviceName = device,
                ip = ip,
            ),
        )

        return userSessionCookie
    }

    private fun upsert(userAuthSessionData: UserAuthSessionData) = userSessionCM.cache(userAuthSessionData)

    fun delete(
        userId: DtId<UserData>,
        sessionId: DtId<UserAuthSessionData>,
    ) = userSessionCM.delete(userId, sessionId)

    fun deleteAllOfUser(userId: DtId<UserData>) = userSessionCM.deleteAllOfUser(userId)
}
