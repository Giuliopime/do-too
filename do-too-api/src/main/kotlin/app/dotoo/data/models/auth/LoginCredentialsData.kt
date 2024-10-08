package app.dotoo.data.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentialsData(
    val email: String,
    val password: String,
)
