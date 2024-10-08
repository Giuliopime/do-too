package app.dotoo.core.logic

import org.koin.core.annotation.Factory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Factory
class PasswordEncoder {
    private val bcryptPasswordEncoder = BCryptPasswordEncoder()

    fun encode(password: String): String = bcryptPasswordEncoder.encode(password)

    fun matches(
        rawPassword: String,
        encodedPassword: String,
    ) = bcryptPasswordEncoder.matches(rawPassword, encodedPassword)
}
