package app.dotoo.data.sources.db.dbi.user

import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.data.models.email.EmailVerificationData
import app.dotoo.data.models.user.UserData
import app.dotoo.data.sources.db.dbi.DBI

interface EmailVerificationDBI : DBI {
    suspend fun count(id: DtId<UserData>): Long

    suspend fun create(emailVerificationData: EmailVerificationData)

    suspend fun get(token: String): EmailVerificationData?

    suspend fun deleteAll(id: DtId<UserData>)

    suspend fun deleteExpired()
}
