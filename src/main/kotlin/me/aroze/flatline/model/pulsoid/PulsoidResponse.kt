package me.aroze.flatline.model.pulsoid

import kotlinx.serialization.Serializable

@Serializable
data class PulsoidResponse(
    val timestamp: Long,
    val data: PulsoidResponseData
)
