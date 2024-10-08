package app.dotoo.core.logic.typedId.impl

import app.dotoo.core.logic.typedId.Id
import java.util.*

/**
 * A [UUID] id.
 */
data class DtIntId<T>(val id: Int) : Id<T> {
    constructor(id: String) : this(id.toInt())

    override fun toString(): String {
        return id.toString()
    }
}
