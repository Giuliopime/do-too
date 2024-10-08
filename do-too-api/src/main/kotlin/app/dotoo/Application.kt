package app.dotoo

import app.dotoo.api.plugins.*
import app.dotoo.api.routing.configureRouting
import app.dotoo.config.ApiConfig
import app.dotoo.config.ApplicationConfig
import app.dotoo.config.core.ConfigurationManager
import app.dotoo.config.core.ConfigurationReader
import ch.qos.logback.classic.Logger
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory

fun main() {
    println("""
         _____     ______        ______   ______     ______    
        /\  __-.  /\  __ \      /\__  _\ /\  __ \   /\  __ \   
        \ \ \/\ \ \ \ \/\ \     \/_/\ \/ \ \ \/\ \  \ \ \/\ \  
         \ \____-  \ \_____\       \ \_\  \ \_____\  \ \_____\ 
          \/____/   \/_____/        \/_/   \/_____/   \/_____/ 
                                                       
    """.trimIndent())

    /**
     * Load configuration properties (environment)
     */
    val configInitializer = ConfigurationManager(
        packageName = ConfigurationManager.DEFAULT_CONFIG_PACKAGE,
        configurationReader = ConfigurationReader::read
    )

    configInitializer.initialize()

    /**
     * Configure logging
     */
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger).level = ApplicationConfig.logLevel

    /**
     * Start api server
     */
    embeddedServer(Netty, port = ApiConfig.port, host = "0.0.0.0", module = Application::indexApplicationModule)
        .start(wait = true)
}

private fun Application.indexApplicationModule() {
    configureHTTP()
    configureSerialization()
    configureSecurity()
    configureStatusPages()
    configureValidator()
    configureRouting()
}