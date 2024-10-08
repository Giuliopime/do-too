package app.dotoo.core.clients

import app.dotoo.config.PostgresConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.annotation.Single

private const val DB_DRIVER = "org.postgresql.Driver"

private val log = KotlinLogging.logger { }

@Single(createdAtStart = true)
class PostgresClient {
    @Suppress("UNUSED")
    private val database = Database.connect(
        url = PostgresConfig.url,
        driver = DB_DRIVER,
        user = PostgresConfig.user,
        password = PostgresConfig.password,
    )

    @Suppress("UNUSED")
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(
            context = Dispatchers.IO,
        ) { block() }

    init {
        runMigrations()
    }

    private fun runMigrations() {
        val flyway = Flyway.configure()
            .driver(DB_DRIVER)
            .dataSource(PostgresConfig.url, PostgresConfig.user, PostgresConfig.password)
            .validateMigrationNaming(true)
            .load()
        try {
            flyway.info()
//            flyway.repair()
            flyway.migrate()
            log.info { "Flyway migration has finished" }
        } catch (e: Exception) {
            log.error(e) { "Exception running flyway migration" }
            throw e
        }
    }
}
