package app.dotoo.data.sources.db.dbi

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

interface DBI {
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(
            context = Dispatchers.IO,
        ) { block() }
}
