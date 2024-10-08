package app.dotoo.core.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PasswordEncoderTest {
    private val passwordEncoder = PasswordEncoder()

    @Test
    fun matches() {
        assertEquals(true, passwordEncoder.matches("Zfre5!h%", passwordEncoder.encode("Zfre5!h%")))
    }
}
