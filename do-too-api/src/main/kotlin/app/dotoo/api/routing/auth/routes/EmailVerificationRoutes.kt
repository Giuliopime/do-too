package app.dotoo.api.routing.auth.routes

import app.dotoo.api.plugins.AuthenticationMethods
import app.dotoo.api.plugins.UserIdPrincipalForEmailVerificationAuth
import app.dotoo.api.routing.auth.IsEmailVerifiedRoute
import app.dotoo.api.routing.auth.SendVerificationEmailRoute
import app.dotoo.api.routing.auth.VerifyEmailRoute
import app.dotoo.config.BrevoConfig
import app.dotoo.core.logic.usecases.EmailVerificationUseCase
import app.dotoo.data.daos.auth.EmailVerificationDao
import app.dotoo.data.daos.user.UserDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.emailVerificationRoutes() {
    val userDao by inject<UserDao>()
    val emailVerificationDao by inject<EmailVerificationDao>()

    authenticate(AuthenticationMethods.EMAIL_VERIFICATION_FORM_AUTH) {
        post<SendVerificationEmailRoute> {
            val userDto = call.principal<UserIdPrincipalForEmailVerificationAuth>()?.id?.let {
                userDao.get(it)
            } ?: return@post call.respond(HttpStatusCode.Forbidden)

            if (userDto.emailVerified) {
                return@post call.respond(HttpStatusCode.OK)
            }

            if (emailVerificationDao.isUserRateLimited(userDto.id)) {
                return@post call.respond(HttpStatusCode.TooManyRequests)
            }

            val emailSent = EmailVerificationUseCase.createAndSend(userDto)

            if (emailSent) {
                call.respond(HttpStatusCode.Created)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        post<IsEmailVerifiedRoute> {
            val userDto =
                call.principal<UserIdPrincipalForEmailVerificationAuth>()?.id?.let {
                    userDao.get(it)
                } ?: return@post call.respond(HttpStatusCode.Forbidden)

            if (userDto.emailVerified) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }

    get<VerifyEmailRoute> { request ->
        val emailVerificationDto = emailVerificationDao.get(request.token)
            ?: return@get call.respondRedirect(BrevoConfig.emailVerificationErrorUrl)

        val userDto = userDao.get(emailVerificationDto.userId)
            ?: return@get call.respond(HttpStatusCode.BadRequest)

        // Check if user is already verified
        if (userDto.emailVerified) {
            return@get call.respondRedirect(BrevoConfig.emailVerificationSuccessUrl)
        }

        userDao.verifyEmail(userDto.id)
        emailVerificationDao.deleteAllOfUser(userDto.id)
        call.respondRedirect(BrevoConfig.emailVerificationSuccessUrl)

    }
}
