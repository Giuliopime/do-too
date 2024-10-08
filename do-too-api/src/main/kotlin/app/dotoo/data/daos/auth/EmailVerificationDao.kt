package app.dotoo.data.daos.auth

import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.data.models.email.EmailVerificationData
import app.dotoo.data.models.user.UserData
import app.dotoo.data.sources.db.dbi.user.EmailVerificationDBI
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class EmailVerificationDao(
    private val emailVerificationDBI: EmailVerificationDBI
) {
    suspend fun create(emailVerificationData: EmailVerificationData) =
        emailVerificationDBI.create(emailVerificationData)

    suspend fun get(token: String): EmailVerificationData? {
        return emailVerificationDBI.get(token)
    }

    /**
     * Deletes all email verification tokens of a specific email.
     */
    suspend fun deleteAllOfUser(id: DtId<UserData>) = emailVerificationDBI.deleteAll(id)

    /**
     * Rate limited if the user has received at least 5 verification emails and didn't use any
     */
    suspend fun isUserRateLimited(id: DtId<UserData>): Boolean {
        return emailVerificationDBI.count(id) >= 5
    }
}
