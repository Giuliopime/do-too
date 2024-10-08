package app.dotoo.core.logic

import org.koin.core.annotation.Factory
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

@Factory
class TokenGenerator {
    private val secureRandom: SecureRandom = SecureRandom() // thread safe
    private val base64Encoder = Base64.getUrlEncoder()
    private val base64Decoder = Base64.getUrlDecoder()

    /**
     * Generates a secure random token
     *
     * @param bytes The number of bytes to use for the token, default is 16
     * @return A pair, where the first value is the token in base64 url safe format, and the second value is the hash of the token also in base64 url safe format
     *
     */
    fun generate(bytes: Int = 16): Pair<String, String> {
        val plainBytes = ByteArray(bytes)
        secureRandom.nextBytes(plainBytes)

        // MessageDigest is not thread safe and getInstance doesn't refer to a singleton
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashed = messageDigest.digest(plainBytes)

        return Pair(base64Encoder.encodeToString(plainBytes), base64Encoder.encodeToString(hashed))
    }

    /**
     * @param token the base 64 non-hashed token
     *
     * @return the base64 url safe hashed token
     */
    fun hashToken(token: String): String {
        val bytes = base64Decoder.decode(token)

        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashed = messageDigest.digest(bytes)

        return base64Encoder.encodeToString(hashed)
    }
}
