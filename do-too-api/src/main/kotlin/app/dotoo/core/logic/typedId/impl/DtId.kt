package app.dotoo.core.logic.typedId.impl

import app.dotoo.core.logic.typedId.Id
import java.util.*

/**
 * A [UUID] id.
 */
data class DtId<T>(val id: UUID) : Id<T> {
    constructor(id: String) : this(UUID.fromString(id))

    override fun toString(): String {
        return id.toString()
    }
}
