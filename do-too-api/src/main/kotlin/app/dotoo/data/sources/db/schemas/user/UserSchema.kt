package app.dotoo.data.sources.db.schemas.user

import app.dotoo.data.models.user.UserData
import app.dotoo.data.sources.db.schemas.user.UsersTable.created_at
import app.dotoo.data.sources.db.schemas.user.UsersTable.email
import app.dotoo.data.sources.db.schemas.user.UsersTable.email_verified
import app.dotoo.data.sources.db.schemas.user.UsersTable.id
import app.dotoo.data.sources.db.schemas.user.UsersTable.password_hash
import app.dotoo.data.sources.db.toDtId
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.*

/**
 * users instead of "user" because the latter is a reserved keyword in postgres
 * @property id
 * @property email
 * @property password_hash
 * @property email_verified
 * @property created_at
 * @property creation_source
 * @property has_pro
 */
object UsersTable : UUIDTable() {
    val email = varchar("email", 150).uniqueIndex()
    val password_hash = varchar("password_hash", 100)
    val email_verified = bool("email_verified")
    val created_at = timestamp("created_at")
}

/**
 * @property id
 * @property email
 * @property password_hash
 * @property email_verified
 * @property created_at
 * @property creation_source
 * @property has_pro
 */
class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UsersTable)

    var email by UsersTable.email
    var password_hash by UsersTable.password_hash
    var email_verified by UsersTable.email_verified
    var created_at by UsersTable.created_at
}

fun UserEntity.fromData(userData: UserData) {
    email = userData.email
    password_hash = userData.passwordHash
    email_verified = userData.emailVerified
    created_at = Instant.ofEpochMilli(userData.creationTimestamp)
}

fun UserEntity.toData() =
    UserData(
        id = id.toDtId(),
        email = email,
        passwordHash = password_hash,
        emailVerified = email_verified,
        creationTimestamp = created_at.toEpochMilli(),
    )
