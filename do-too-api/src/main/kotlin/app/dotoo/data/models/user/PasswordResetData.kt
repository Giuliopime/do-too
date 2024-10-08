package app.dotoo.data.models.user

import app.dotoo.core.logic.DatetimeUtils
import app.dotoo.core.logic.typedId.impl.DtId
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * @param token should be randomly generated and hashed
 */
@Serializable
data class PasswordResetData(
    val token: String,
    @Contextual val userId: DtId<UserData>,
    @Contextual val expireAt: Long,
    @Contextual val createdAt: Long = DatetimeUtils.currentMillis(),
)
