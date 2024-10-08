package app.dotoo.data.sources.db

import app.dotoo.core.logic.typedId.impl.DtId
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import java.util.*

fun <T> EntityID<UUID>.toDtId(): DtId<T> = DtId(value)

fun DtId<*>.toEntityId(table: IdTable<UUID>) = EntityID(this.id, table)