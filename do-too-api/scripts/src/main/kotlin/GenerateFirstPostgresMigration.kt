import app.dotoo.config.PostgresConfig
import app.dotoo.config.core.ConfigurationManager
import app.dotoo.config.core.ConfigurationReader
import app.dotoo.data.sources.db.schemas.user.EmailVerificationTable
import app.dotoo.data.sources.db.schemas.user.PasswordResetTable
import app.dotoo.data.sources.db.schemas.user.UsersTable
import core.createScriptOutputsFolderIfNotExisting
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

private const val DB_DRIVER = "org.postgresql.Driver"

/**
 * Script that generates the first migration for the database schema
 * Result file should be put in /resources/db/migration/V1__create_db.sql
 */
fun main() {
    ConfigurationManager(ConfigurationManager.DEFAULT_CONFIG_PACKAGE, ConfigurationReader::read).initialize()

    Database.connect(
        url = PostgresConfig.url,
        driver = DB_DRIVER,
        user = PostgresConfig.user,
        password = PostgresConfig.password
    )

    val statements = transaction {
        SchemaUtils.createStatements(
            UsersTable,
            PasswordResetTable,
            EmailVerificationTable,
        )
    }

    val folder = createScriptOutputsFolderIfNotExisting()
    val file = File(folder, "V1__create_db.sql")
    file.writeText(statements.joinToString("\n") { "$it;"} )
}