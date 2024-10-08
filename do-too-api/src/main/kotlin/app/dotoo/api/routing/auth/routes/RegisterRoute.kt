package app.dotoo.api.routing.auth.routes

import app.dotoo.api.routing.auth.RegisterRoute
import app.dotoo.core.logic.DatetimeUtils
import app.dotoo.core.logic.PasswordEncoder
import app.dotoo.core.logic.typedId.newDtId
import app.dotoo.core.logic.usecases.EmailVerificationUseCase
import app.dotoo.core.logic.usecases.UserAuthUseCase
import app.dotoo.data.daos.user.UserDao
import app.dotoo.data.models.auth.RegistrationCredentials
import app.dotoo.data.models.user.UserData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.registerRoute() {
    val userDao by inject<UserDao>()
    val passwordEncoder by inject<PasswordEncoder>()

    /**
     * When a user registers, he needs to set an email and password,
     * and he will be able to log in into his account only once he has verified the email
     */
    post<RegisterRoute> {
        val signupData = call.receive<RegistrationCredentials>()

        val existingUser = userDao.getFromEmail(signupData.email)

        if (existingUser != null) {
            if (UserAuthUseCase.isIncompleteAccountOutdated(existingUser)) {
                userDao.delete(existingUser.id)
            } else {
                call.respond(HttpStatusCode.Forbidden)
                return@post
            }
        }

        val hashedPassword = passwordEncoder.encode(signupData.password)
        val user = UserData(
            id = newDtId(),
            email = signupData.email,
            passwordHash = hashedPassword,
            emailVerified = false,
            creationTimestamp = DatetimeUtils.currentMillis()
        )

        userDao.create(user)

        val emailSent = EmailVerificationUseCase.createAndSend(user)

        if (emailSent) {
            // User will need to verify its email
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.Created)
        }
    }
}
