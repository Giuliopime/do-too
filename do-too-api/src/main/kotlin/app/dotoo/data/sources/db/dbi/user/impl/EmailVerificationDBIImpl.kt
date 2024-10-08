package app.dotoo.data.sources.db.dbi.user.impl

import app.dotoo.core.logic.DatetimeUtils
import app.dotoo.core.logic.TokenGenerator
import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.data.models.email.EmailVerificationData
import app.dotoo.data.models.user.UserData
import app.dotoo.data.sources.db.dbi.user.EmailVerificationDBI
import app.dotoo.data.sources.db.schemas.user.*
import app.dotoo.data.sources.db.toEntityId
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class EmailVerificationDBIImpl(
    private val tokenGenerator: TokenGenerator,
) : EmailVerificationDBI {
    override suspend fun count(id: DtId<UserData>): Long =
        dbQuery {
            val currentMillis = DatetimeUtils.currentJavaInstant()

            EmailVerificationEntity.count(
                EmailVerificationTable.user eq id.toEntityId(UsersTable)
                        and (EmailVerificationTable.expires_at greater currentMillis)
            )
        }

    override suspend fun create(emailVerificationData: EmailVerificationData) {
        dbQuery {
            EmailVerificationEntity.new {
                fromData(emailVerificationData)
            }
        }
    }

    override suspend fun get(token: String) =
        dbQuery {
            EmailVerificationEntity
                .find { EmailVerificationTable.token eq tokenGenerator.hashToken(token) }
                .limit(1)
                .firstOrNull()
                ?.toData()
        }

    override suspend fun deleteAll(id: DtId<UserData>) {
        dbQuery {
            EmailVerificationTable.deleteWhere { user eq id.toEntityId(UsersTable) }
        }
    }

    override suspend fun deleteExpired() {
        dbQuery {
            val currentTimestamp = DatetimeUtils.currentJavaInstant()

            EmailVerificationTable.deleteWhere {
                expires_at less currentTimestamp
            }
        }
    }
}
