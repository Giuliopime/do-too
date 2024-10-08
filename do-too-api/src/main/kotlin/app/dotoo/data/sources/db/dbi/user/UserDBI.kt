package app.dotoo.data.sources.db.dbi.user

import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.data.models.user.UserData
import app.dotoo.data.sources.db.dbi.DBI

interface UserDBI : DBI {
    suspend fun create(userData: UserData)

    suspend fun get(id: DtId<UserData>): UserData?

    suspend fun get(email: String): UserData?

    suspend fun verifyEmail(id: DtId<UserData>)

    suspend fun changePassword(
        id: DtId<UserData>,
        newPasswordHashed: String,
    )

    suspend fun resetPassword(
        id: DtId<UserData>,
        newPasswordHashed: String,
        verifyEmail: Boolean,
    )

    suspend fun delete(id: DtId<UserData>)
}
