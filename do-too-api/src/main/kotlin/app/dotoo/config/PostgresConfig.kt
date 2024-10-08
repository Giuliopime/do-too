package app.dotoo.config

import app.dotoo.config.core.Configuration
import app.dotoo.config.core.ConfigurationProperty

@Configuration("postgres")
object PostgresConfig {
    @ConfigurationProperty("url")
    var url: String = "jdbc:postgresql://localhost:5432/dotoodevdb"

    @ConfigurationProperty("user")
    var user: String = "DoTooDevUser"

    @ConfigurationProperty("password")
    var password: String = "DoTooDevPassword"
}
