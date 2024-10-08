package app.dotoo.api.plugins

import app.dotoo.config.ApiConfig
import app.dotoo.core.exceptions.AuthenticationException
import app.dotoo.core.logic.DatetimeUtils
import app.dotoo.core.logic.PasswordEncoder
import app.dotoo.core.logic.typedId.impl.DtId
import app.dotoo.core.logic.typedId.serialization.IdKotlinXSerializationModule
import app.dotoo.data.daos.auth.UserSessionDao
import app.dotoo.data.daos.user.UserDao
import app.dotoo.data.models.auth.UserAuthSessionData
import app.dotoo.data.models.auth.UserSessionCookie
import app.dotoo.data.models.user.UserData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.server.sessions.serialization.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

/**
 * Available authentication methods for api routes
 */
object AuthenticationMethods {
    const val EMAIL_VERIFICATION_FORM_AUTH = "email_verification_form_auth"
    const val USER_SESSION_AUTH = "user_session_auth"
}

/**
 * Used to store the Id in the email verification routes (that cannot use proper session authentication)
 */
data class UserIdPrincipalForEmailVerificationAuth(val id: DtId<UserData>) : Principal

fun Application.configureSecurity() {
    val userDao by inject<UserDao>()
    val userSessionDao by inject<UserSessionDao>()
    val passwordEncoder by inject<PasswordEncoder>()

    install(Sessions) {
        cookie<UserSessionCookie>("user_session_id") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = ApiConfig.sessionMaxAgeInSeconds
            cookie.secure = ApiConfig.cookieSecure
            cookie.httpOnly = true
            cookie.extensions["SameSite"] = "None"

            serializer =
                KotlinxSessionSerializer(
                    Json {
                        serializersModule = IdKotlinXSerializationModule
                    },
                )
        }
    }

    install(Authentication) {
        // Used only for email verification operation
        form(AuthenticationMethods.EMAIL_VERIFICATION_FORM_AUTH) {
            userParamName = "email"
            passwordParamName = "password"
            validate { credentials ->
                userDao.getFromEmail(credentials.name)?.let {
                    if (passwordEncoder.matches(credentials.password, it.passwordHash)) {
                        UserIdPrincipalForEmailVerificationAuth(it.id)
                    } else {
                        null
                    }
                }
            }
            challenge {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        session<UserSessionCookie>(AuthenticationMethods.USER_SESSION_AUTH) {
            validate { userSessionCookie ->
                val session = userSessionDao.get(userSessionCookie.user_id, userSessionCookie.session_id)

                // If there is no session or if it has expired
                if (session == null || (DatetimeUtils.currentMillis() - session.iat) >= (ApiConfig.sessionMaxAgeInSeconds * 1000)) {
                    null
                } else {
                    session
                }
            }
            challenge {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}

/**
 * Gets the current [UserAuthSessionData]
 */
fun PipelineContext<Unit, ApplicationCall>.authSessionData(): UserAuthSessionData? = call.principal<UserAuthSessionData>()

/**
 * Gets the current [UserAuthSessionData]
 *
 * @throws AuthenticationException if not authenticated
 */
fun PipelineContext<Unit, ApplicationCall>.authSessionDataOrThrow(): UserAuthSessionData = authSessionData() ?: throw AuthenticationException()


/**
 * Gets the [DtId] of the session authenticated [UserData]
 */
fun PipelineContext<Unit, ApplicationCall>.userIdFromSession(): DtId<UserData>? = call.principal<UserAuthSessionData>()?.userId

/**
 * Gets the [DtId] of the session authenticated [UserData]
 *
 * @throws AuthenticationException if not authenticated
 */
fun PipelineContext<Unit, ApplicationCall>.userIdFromSessionOrThrow(): DtId<UserData> = userIdFromSession() ?: throw AuthenticationException()
