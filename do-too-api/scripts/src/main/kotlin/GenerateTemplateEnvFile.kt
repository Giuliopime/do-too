import app.dotoo.config.core.ConfigurationManager
import app.dotoo.config.core.ConfigurationReader
import core.createScriptOutputsFolderIfNotExisting
import java.io.File

/**
 * Script that generates a template .env file based on the declared @Configuration objects
 */
fun main() {
    val configs = ConfigurationManager(
        packageName = ConfigurationManager.DEFAULT_CONFIG_PACKAGE,
        configurationReader = ConfigurationReader::read
    ).listConfigurations()

    val folder = createScriptOutputsFolderIfNotExisting()
    val file = File(folder, ".env.template")
    file.writeText(configs.joinToString("\n") { it.toString() } )
}