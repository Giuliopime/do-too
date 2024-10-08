package app.dotoo.config

import app.dotoo.config.core.Configuration
import app.dotoo.config.core.ConfigurationProperty

@Configuration("brevo")
object BrevoConfig {
    @ConfigurationProperty("api.key")
    var apiKey: String = "none"

    @ConfigurationProperty("template.email.verification")
    var emailVerificationTemplateId: Long = 2

    @ConfigurationProperty("template.password.reset")
    var passwordResetTemplateId: Long = 1

    @ConfigurationProperty("template.password.reset.success")
    var passwordResetSuccessTemplateId: Long = 3

    @ConfigurationProperty("email.verification.success.url")
    var emailVerificationSuccessUrl: String = "https://dotoo.app/email-verified"

    @ConfigurationProperty("email.verification.error.url")
    var emailVerificationErrorUrl: String = "https://dotoo.app/invalid-email-verification-code"

    @ConfigurationProperty("email.verification.url")
    var emailVerificationUrl: String = "http://localhost:8080/verify-email"

    @ConfigurationProperty("reset.password.url")
    var passwordResetUrl: String = "https://dotoo.app/reset-password"
}
