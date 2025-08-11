package me.aroze.flatline.model.pulsoid

import kotlinx.serialization.Serializable

@Serializable
data class PulsoidResponseData(
    val heartRate: Int
)
