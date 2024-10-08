package app.dotoo.api.routing.auth.routes

import app.dotoo.api.routing.auth.LoginRoute
import app.dotoo.core.exceptions.AuthenticationException
import app.dotoo.core.logic.PasswordEncoder
import app.dotoo.data.daos.auth.UserSessionDao
import app.dotoo.data.daos.user.UserDao
import app.dotoo.data.models.auth.LoginCredentialsData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.loginRoute() {
    val userDao by inject<UserDao>()
    val userSessionDao by inject<UserSessionDao>()
    val passwordEncoder by inject<PasswordEncoder>()

    post<LoginRoute> {
        val loginData = call.receive<LoginCredentialsData>()
        val user = userDao.getFromEmail(loginData.email)
            ?: throw AuthenticationException()

        if (!passwordEncoder.matches(loginData.password, user.passwordHash)) {
            throw AuthenticationException()
        }

        // User email must be verified
        if (!user.emailVerified) {
            return@post call.respond(HttpStatusCode.MethodNotAllowed)
        }

        val userSessionId = userSessionDao.create(
            userId = user.id,
            device = call.request.userAgent(),
            ip = call.request.origin.remoteAddress
        )

        call.sessions.set(userSessionId)
        call.respond(user.getResponseDto())
    }
}
