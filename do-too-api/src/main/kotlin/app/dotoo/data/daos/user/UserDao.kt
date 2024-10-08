package app.dotoo.data.daos.user

import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.data.models.user.UserData
import app.dotoo.data.sources.db.dbi.user.UserDBI
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class UserDao(
    private val userDBI: UserDBI,
) {
    suspend fun create(userData: UserData) {
        userDBI.create(userData)
    }

    suspend fun get(id: DtId<UserData>): UserData? {
        return userDBI.get(id)
    }

    /**
     * This method should be only used in the login route or list invitation route
     */
    suspend fun getFromEmail(email: String): UserData? {
        return userDBI.get(email)
    }

    suspend fun verifyEmail(id: DtId<UserData>) {
        userDBI.verifyEmail(id)
    }

    suspend fun resetPassword(
        id: DtId<UserData>,
        newPasswordHashed: String,
        verifyEmail: Boolean,
    ) {
        userDBI.resetPassword(id, newPasswordHashed, verifyEmail)
    }

    suspend fun delete(id: DtId<UserData>) {
        userDBI.delete(id)
    }
}
