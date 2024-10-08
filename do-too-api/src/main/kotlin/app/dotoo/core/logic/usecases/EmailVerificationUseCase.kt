package app.dotoo.core.logic.usecases

import app.dotoo.core.clients.BrevoClient
import app.dotoo.core.logic.DatetimeUtils
import app.dotoo.core.logic.TokenGenerator
import app.dotoo.data.daos.auth.EmailVerificationDao
import app.dotoo.data.models.email.EmailVerificationData
import app.dotoo.data.models.user.UserData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object EmailVerificationUseCase : KoinComponent {
    private val emailVerificationDao by inject<EmailVerificationDao>()
    private val tokenGenerator by inject<TokenGenerator>()
    private val brevoClient by inject<BrevoClient>()

    /**
     * Sends a verification email to the provided email
     * and returns true if the email was sent successfully, false otherwise
     *
     * @return true if the email was sent, false otherwise
     */
    suspend fun createAndSend(user: UserData): Boolean {
        val (token, hashedToken) = tokenGenerator.generate()

        val emailVerificationData = EmailVerificationData(
            token = hashedToken,
            userId = user.id,
            expireAt = DatetimeUtils.currentMillis() + 3600000,
            createdAt = DatetimeUtils.currentMillis(),
        )

        val sent = brevoClient.sendEmailVerificationEmail(user.email, token)

        if (sent) {
            emailVerificationDao.create(emailVerificationData)
        }

        return sent
    }
}