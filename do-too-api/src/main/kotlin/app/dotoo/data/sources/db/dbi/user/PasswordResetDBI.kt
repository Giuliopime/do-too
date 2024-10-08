package app.dotoo.data.sources.db.dbi.user

import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.data.models.user.PasswordResetData
import app.dotoo.data.models.user.UserData
import app.dotoo.data.sources.db.dbi.DBI

interface PasswordResetDBI : DBI {
    suspend fun count(id: DtId<UserData>): Long

    suspend fun create(passwordResetData: PasswordResetData)

    suspend fun get(token: String): PasswordResetData?

    suspend fun deleteAll(id: DtId<UserData>)

    suspend fun deleteExpired()
}
