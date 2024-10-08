package app.dotoo.data.sources.db.schemas.user

import app.dotoo.data.models.email.EmailVerificationData
import app.dotoo.data.sources.db.schemas.user.EmailVerificationTable.created_at
import app.dotoo.data.sources.db.schemas.user.EmailVerificationTable.expires_at
import app.dotoo.data.sources.db.schemas.user.EmailVerificationTable.id
import app.dotoo.data.sources.db.schemas.user.EmailVerificationTable.token
import app.dotoo.data.sources.db.schemas.user.EmailVerificationTable.user
import app.dotoo.data.sources.db.toDtId
import app.dotoo.data.sources.db.toEntityId
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

/**
 * @property id
 * @property token
 * @property user
 * @property created_at
 * @property expires_at
 */
object EmailVerificationTable : IntIdTable() {
    val token = varchar("token", 100).uniqueIndex()
    val user = reference(
        name = "id_user",
        foreign = UsersTable,
        onDelete = ReferenceOption.CASCADE,
    ).index()
    val created_at = timestamp("created_at")
    val expires_at = timestamp("expires_at")
}

/**
 * @property id
 * @property token
 * @property user
 * @property created_at
 * @property expires_at
 * @property userEntity
 */
class EmailVerificationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EmailVerificationEntity>(EmailVerificationTable)

    var token by EmailVerificationTable.token
    var user by EmailVerificationTable.user
    var created_at by EmailVerificationTable.created_at
    var expires_at by EmailVerificationTable.expires_at

    var userEntity by UserEntity referencedOn EmailVerificationTable.user
}

fun EmailVerificationEntity.fromData(emailVerificationData: EmailVerificationData) {
    token = emailVerificationData.token
    user = emailVerificationData.userId.toEntityId(UsersTable)
    created_at = Instant.ofEpochMilli(emailVerificationData.createdAt)
    expires_at = Instant.ofEpochMilli(emailVerificationData.expireAt)
}

fun EmailVerificationEntity.toData() =
    EmailVerificationData(
        token = token,
        userId = user.toDtId(),
        expireAt = expires_at.toEpochMilli(),
        createdAt = created_at.toEpochMilli(),
    )
