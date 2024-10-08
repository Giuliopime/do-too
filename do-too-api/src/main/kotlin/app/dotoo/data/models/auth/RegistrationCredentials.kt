package app.dotoo.data.models.auth

import app.dotoo.data.validation.RegexPatterns
import app.dotoo.data.validation.Validatable
import io.konform.validation.Validation
import io.konform.validation.ValidationResult
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class RegistrationCredentials(
    val email: String,
    val password: String,
) : Validatable<RegistrationCredentials> {
    override fun validate(): ValidationResult<RegistrationCredentials> =
        Validation {
            RegistrationCredentials::email {
                pattern(RegexPatterns.emailPattern) hint "Please provide a valid email address"
            }
            RegistrationCredentials::password {
                minLength(8) hint "Password min length is 8 characters"
                maxLength(100) hint "Password max length is 100 characters"
                pattern(RegexPatterns.passwordPatterns) hint "Password needs at least an uppercase character, a lowercase one and a number"
            }
        }.invoke(this)
}
