package app.dotoo.core.logic.typedId

import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.core.logic.typedId.impl.DtIntId
import java.util.*

fun <T> newDtId() = DtId<T>(UUID.randomUUID())

fun <T> newDtIntId() = DtIntId<T>((1..100).random())
