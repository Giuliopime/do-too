package app.dotoo.api.plugins

import app.dotoo.config.ApplicationConfig
import app.dotoo.di.ClientModule
import app.dotoo.di.DataModule
import app.dotoo.di.IClosableComponent
import app.dotoo.di.LogicModule
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import org.koin.core.logger.Level
import org.koin.ksp.generated.module
import org.koin.ktor.ext.getKoin
import org.koin.ktor.plugin.Koin
import org.koin.ktor.plugin.KoinApplicationStarted
import org.koin.ktor.plugin.KoinApplicationStopPreparing
import org.koin.ktor.plugin.KoinApplicationStopped
import org.koin.logger.slf4jLogger

private val logger = KotlinLogging.logger {  }

/**
 * Configures dependency injection and graceful shutdown
 */
fun Application.configureDI() {
    install(Koin) {
        slf4jLogger(Level.valueOf(ApplicationConfig.logLevel.levelStr))

        modules(LogicModule().module, ClientModule().module, DataModule().module)

        this.createEagerInstances()
    }

    environment.monitor.subscribe(KoinApplicationStarted) {
        logger.info { "Koin application started" }
    }

    environment.monitor.subscribe(KoinApplicationStopPreparing) {
        logger.info { "Shutdown started" }

        val closableComponents by lazy {
            getKoin().getAll<IClosableComponent>()
        }

        closableComponents.forEach {
            runBlocking {
                it.close()
            }
        }
    }

    environment.monitor.subscribe(KoinApplicationStopped) {
        logger.info { "Shutdown completed gracefully" }
    }
}
