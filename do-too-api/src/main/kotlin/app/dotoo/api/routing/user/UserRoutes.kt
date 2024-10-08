package app.dotoo.api.routing.user

import app.dotoo.api.plugins.AuthenticationMethods
import app.dotoo.api.routing.user.routes.logoutRoutes
import app.dotoo.api.routing.user.routes.passwordOperationRoutes
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

@Resource("/logout")
class LogoutRoute

@Resource("/password-forgotten")
class PasswordForgottenRoute(val email: String)

@Resource("/reset-password")
class ResetPasswordRoute(val token: String)

fun Route.userRoutes() {
    passwordOperationRoutes()

    authenticate(AuthenticationMethods.USER_SESSION_AUTH) {
        logoutRoutes()
    }
}
