package app.dotoo.api.routing.auth

import app.dotoo.api.routing.auth.routes.emailVerificationRoutes
import app.dotoo.api.routing.auth.routes.loginRoute
import app.dotoo.api.routing.auth.routes.registerRoute
import io.ktor.resources.*
import io.ktor.server.routing.*

@Resource("/register")
class RegisterRoute

@Resource("/send-verification-email")
class SendVerificationEmailRoute

@Resource("/verify-email")
class VerifyEmailRoute(val token: String)

@Resource("/is-email-verified")
class IsEmailVerifiedRoute

@Resource("/login")
class LoginRoute

fun Route.authRoutes() {
    registerRoute()
    emailVerificationRoutes()
    loginRoute()
}
