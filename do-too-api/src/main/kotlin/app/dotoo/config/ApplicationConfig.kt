package app.dotoo.config

import app.dotoo.config.core.Configuration
import app.dotoo.config.core.ConfigurationProperty
import ch.qos.logback.classic.Level

@Configuration("application")
object ApplicationConfig {
    @ConfigurationProperty("log.level")
    var logLevel: Level = Level.INFO
}
