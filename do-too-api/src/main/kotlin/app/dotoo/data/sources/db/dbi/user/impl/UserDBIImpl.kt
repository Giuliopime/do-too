package app.dotoo.data.sources.db.dbi.user.impl

import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.data.models.user.UserData
import app.dotoo.data.sources.db.dbi.user.UserDBI
import app.dotoo.data.sources.db.schemas.user.UserEntity
import app.dotoo.data.sources.db.schemas.user.UsersTable
import app.dotoo.data.sources.db.schemas.user.fromData
import app.dotoo.data.sources.db.schemas.user.toData
import app.dotoo.data.sources.db.toEntityId
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class UserDBIImpl : UserDBI {
    override suspend fun create(userData: UserData) {
        dbQuery {
            UserEntity.new(userData.id.id) {
                fromData(userData)
            }
        }
    }

    override suspend fun get(id: DtId<UserData>): UserData? =
        dbQuery {
            UserEntity.findById(id.id)
        }?.toData()

    override suspend fun get(email: String): UserData? =
        dbQuery {
            UserEntity
                .find { UsersTable.email eq email }
                .limit(1)
                .firstOrNull()
                ?.toData()
        }

    override suspend fun verifyEmail(id: DtId<UserData>) {
        dbQuery {
            UsersTable.update({ UsersTable.id eq id.toEntityId(UsersTable) }) {
                it[email_verified] = true
            }
        }
    }

    override suspend fun changePassword(id: DtId<UserData>, newPasswordHashed: String) {
        dbQuery {
            UsersTable.update({ UsersTable.id eq id.toEntityId(UsersTable) }) {
                it[password_hash] = newPasswordHashed
            }
        }
    }

    override suspend fun resetPassword(
        id: DtId<UserData>,
        newPasswordHashed: String,
        verifyEmail: Boolean,
    ) {
        dbQuery {
            UsersTable.update({ UsersTable.id eq id.toEntityId(UsersTable) }) {
                if (verifyEmail) {
                    it[email_verified] = true
                }
                it[password_hash] = newPasswordHashed
            }
        }
    }

    override suspend fun delete(id: DtId<UserData>) {
        dbQuery {
            UsersTable.deleteWhere { UsersTable.id eq id.toEntityId(UsersTable) }
        }
    }
}
