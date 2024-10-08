package app.dotoo.data.sources.db.schemas.user

import app.dotoo.data.models.user.PasswordResetData
import app.dotoo.data.sources.db.schemas.user.PasswordResetTable.created_at
import app.dotoo.data.sources.db.schemas.user.PasswordResetTable.expires_at
import app.dotoo.data.sources.db.schemas.user.PasswordResetTable.id
import app.dotoo.data.sources.db.schemas.user.PasswordResetTable.token
import app.dotoo.data.sources.db.schemas.user.PasswordResetTable.user
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
object PasswordResetTable : IntIdTable() {
    val token = varchar("token", 100).uniqueIndex()
    val user =
        reference(
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
class PasswordResetEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PasswordResetEntity>(PasswordResetTable)

    var token by PasswordResetTable.token
    var user by PasswordResetTable.user
    var created_at by PasswordResetTable.created_at
    var expires_at by PasswordResetTable.expires_at

    val userEntity by UserEntity referencedOn PasswordResetTable.user
}

fun PasswordResetEntity.fromData(passwordResetData: PasswordResetData) {
    token = passwordResetData.token
    user = passwordResetData.userId.toEntityId(UsersTable)
    created_at = Instant.ofEpochMilli(passwordResetData.createdAt)
    expires_at = Instant.ofEpochMilli(passwordResetData.expireAt)
}

fun PasswordResetEntity.toData() =
    PasswordResetData(
        token = token,
        userId = user.toDtId(),
        createdAt = created_at.toEpochMilli(),
        expireAt = expires_at.toEpochMilli(),
    )
