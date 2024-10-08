package app.dotoo.api.routing.user.routes

import app.dotoo.api.routing.user.LogoutRoute
import app.dotoo.data.daos.auth.UserSessionDao
import app.dotoo.data.models.auth.UserSessionCookie
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.logoutRoutes() {
    val userSessionDao by inject<UserSessionDao>()

    get<LogoutRoute> {
        val session = call.sessions.get<UserSessionCookie>()!!

        userSessionDao.delete(session.user_id, session.session_id)

        call.sessions.clear<UserSessionCookie>()
        call.respond(HttpStatusCode.OK)
    }
}
