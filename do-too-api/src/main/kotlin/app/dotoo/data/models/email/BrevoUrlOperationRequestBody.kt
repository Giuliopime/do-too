package app.dotoo.data.models.email

import kotlinx.serialization.Serializable

@Serializable
data class BrevoUrlOperationRequestBody(
    val to: List<BrevoEmailField>,
    val templateId: Long,
    val params: Params,
) {
    @Serializable
    data class Params(
        val url: String,
    )
}