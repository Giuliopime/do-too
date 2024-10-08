package app.dotoo.api.routing.user.routes

import app.dotoo.api.routing.user.PasswordForgottenRoute
import app.dotoo.api.routing.user.ResetPasswordRoute
import app.dotoo.core.clients.BrevoClient
import app.dotoo.core.logic.PasswordEncoder
import app.dotoo.core.logic.usecases.PasswordResetUseCase
import app.dotoo.data.daos.auth.PasswordResetDao
import app.dotoo.data.daos.auth.UserSessionDao
import app.dotoo.data.daos.user.UserDao
import app.dotoo.data.models.auth.PasswordResetRequestBody
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.passwordOperationRoutes() {
    val userDao by inject<UserDao>()
    val userSessionDao by inject<UserSessionDao>()
    val passwordResetDao by inject<PasswordResetDao>()
    val passwordEncoder by inject<PasswordEncoder>()
    val brevoClient by inject<BrevoClient>()

    get<PasswordForgottenRoute> { request ->
        val user = userDao.getFromEmail(request.email)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        if (passwordResetDao.isUserRateLimited(user.id)) {
            return@get call.respond(HttpStatusCode.TooManyRequests)
        }

        val sentEmail = PasswordResetUseCase.createAndSend(user)

        if (sentEmail) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    post<ResetPasswordRoute> { request ->
        val passwordResetDto = passwordResetDao.get(request.token)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val user = userDao.get(passwordResetDto.userId)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val newPassword = call.receive<PasswordResetRequestBody>().password
        val newPasswordHashed = passwordEncoder.encode(newPassword)

        // If the user email wasn't verified before, now it can be considered verified
        userDao.resetPassword(
            id = passwordResetDto.userId,
            newPasswordHashed = newPasswordHashed,
            verifyEmail = true
        )

        // Invalidate all other user active sessions
        userSessionDao.deleteAllOfUser(passwordResetDto.userId)

        // Send notification email
        brevoClient.sendPasswordResetSuccessEmail(user.email)

        call.respond(HttpStatusCode.OK)
    }
}
