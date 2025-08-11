package me.aroze.flatline.model

import net.kyori.adventure.bossbar.BossBar
import okhttp3.WebSocket
import java.util.UUID

data class HeartbeatData(
    val uuid: UUID,
    val accessToken: UUID,
    val socket: WebSocket,
    var bpm: Int? = null,
    var bossbar: BossBar? = null,
    var lastHeartbeatTick: Int = 0
)
