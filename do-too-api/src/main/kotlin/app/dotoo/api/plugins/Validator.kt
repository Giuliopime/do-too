package app.dotoo.api.plugins

import app.dotoo.data.models.auth.PasswordResetRequestBody
import app.dotoo.data.models.auth.RegistrationCredentials
import app.dotoo.data.validation.Validatable
import io.konform.validation.Valid
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidator() {
    install(RequestValidation) {
        validateValidatable<RegistrationCredentials>()
        validateValidatable<PasswordResetRequestBody>()
    }
}

inline fun <reified T : Validatable<T>> RequestValidationConfig.validateValidatable() =
    validate<T> {
        val validationResult = it.validate()
        if (validationResult is Valid) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(
                validationResult.errors.map { error -> "${error.dataPath}: ${error.message}" },
            )
        }
    }
