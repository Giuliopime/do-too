package app.dotoo.api.routing

import app.dotoo.api.routing.auth.authRoutes
import app.dotoo.api.routing.user.userRoutes
import app.dotoo.core.logic.typedId.serialization.IdKotlinXSerializationModule
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    // Needed for typed queries
    install(Resources) {
        serializersModule = IdKotlinXSerializationModule
    }

    routing {
        authRoutes()
        userRoutes()
    }
}
