package app.dotoo.data.sources.cache.cm.users

import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.data.models.auth.UserAuthSessionData
import app.dotoo.data.models.user.UserData

interface UserSessionCM {
    fun get(
        userId: DtId<UserData>,
        sessionId: DtId<UserAuthSessionData>,
    ): UserAuthSessionData?

    fun cache(userAuthSessionData: UserAuthSessionData)

    fun delete(
        userId: DtId<UserData>,
        sessionId: DtId<UserAuthSessionData>,
    )

    fun deleteAllOfUser(userId: DtId<UserData>)
}
