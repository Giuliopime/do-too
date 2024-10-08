package app.dotoo.models.auth

import app.dotoo.data.models.auth.RegistrationCredentials
import io.konform.validation.Valid
import org.junit.jupiter.api.Test

class TokenCredentialsTest {
    @Test
    fun validate() {
        val invalidCredentials1 =
            RegistrationCredentials(
                "test@gmail.com",
                "invalidpassword",
            )

        val invalidCredentials2 =
            RegistrationCredentials(
                "test@gmail.com",
                "invalidPassword",
            )

        val invalidCredentials3 =
            RegistrationCredentials(
                "test@gmail.com",
                "invalidpassword1",
            )

        val validCredentials =
            RegistrationCredentials(
                "test@gmail.com",
                "validPassword1",
            )

        val validCredentials2 =
            RegistrationCredentials(
                "test@gmail.com",
                "vAli4Passw0rD987",
            )

        assert(invalidCredentials1.validate() !is Valid)
        assert(invalidCredentials2.validate() !is Valid)
        assert(invalidCredentials3.validate() !is Valid)

        assert(validCredentials.validate() is Valid)
        assert(validCredentials2.validate() is Valid)
    }
}
