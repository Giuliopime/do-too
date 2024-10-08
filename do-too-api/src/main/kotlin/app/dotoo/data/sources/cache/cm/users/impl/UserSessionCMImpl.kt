package app.dotoo.data.sources.cache.cm.users.impl

import app.dotoo.config.ApiConfig
import app.dotoo.core.clients.RedisClient
import app.dotoo.core.logic.ObjectMapper
import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.data.models.auth.UserAuthSessionData
import app.dotoo.data.models.user.UserData
import app.dotoo.data.sources.cache.cm.users.UserSessionCM
import app.dotoo.data.sources.cache.core.ExpiringCM
import org.koin.core.annotation.Single

@Single(createdAtStart = true, binds = [UserSessionCM::class])
class UserSessionCMImpl(
    redisClient: RedisClient,
    objectMapper: ObjectMapper,
) : UserSessionCM,
    ExpiringCM(
        keyBase = "sessions",
        expirationInSeconds = (ApiConfig.sessionMaxAgeInSeconds + 10),
        redisClient,
        objectMapper,
    ) {
    private fun keyValue(
        userId: DtId<UserData>,
        sessionId: DtId<UserAuthSessionData>,
    ) = "$userId:$sessionId"

    override fun get(
        userId: DtId<UserData>,
        sessionId: DtId<UserAuthSessionData>,
    ): UserAuthSessionData? =
        get(
            keyValue(
                userId,
                sessionId,
            ),
        )

    override fun cache(userAuthSessionData: UserAuthSessionData) =
        cache(keyValue(userAuthSessionData.userId, userAuthSessionData.id), userAuthSessionData)

    override fun delete(
        userId: DtId<UserData>,
        sessionId: DtId<UserAuthSessionData>,
    ) = delete(
        keyValue(
            userId,
            sessionId,
        ),
    )

    override fun deleteAllOfUser(userId: DtId<UserData>) = delete("$userId:*")
}
