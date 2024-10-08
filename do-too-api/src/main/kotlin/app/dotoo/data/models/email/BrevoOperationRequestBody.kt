package app.dotoo.data.models.email

import kotlinx.serialization.Serializable

@Serializable
data class BrevoOperationRequestBody(
    val to: List<BrevoEmailField>,
    val templateId: Long,
)