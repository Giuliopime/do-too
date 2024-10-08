package app.dotoo.config

import app.dotoo.config.core.Configuration
import app.dotoo.config.core.ConfigurationProperty

@Configuration("redis")
object RedisConfig {
    @ConfigurationProperty("connection.string")
    var connectionString: String = "redis://localhost:6379"
}
