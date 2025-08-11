package me.aroze.flatline.registry

import PulsoidWebSocketListener
import me.aroze.flatline.flatline
import me.aroze.flatline.model.HeartbeatData
import okhttp3.Request
import java.util.UUID

object HeartbeatRegistry {

    private val heartBeatData = mutableMapOf<UUID, HeartbeatData>()

    fun getOrLoad(uuid: UUID): HeartbeatData? {
        val heartbeatData = heartBeatData[uuid]
        if (heartbeatData != null) return heartbeatData

        val savedToken = flatline.accessTokens.getString(uuid.toString())
            ?: return null

        val savedTokenUUID = try {
            UUID.fromString(savedToken)
        } catch (e: IllegalArgumentException) {
            flatline.logger.severe("Invalid access token for UUID $uuid: $savedToken")
            return null
        }

        return set(uuid, savedTokenUUID)
    }

    fun getLoaded(uuid: UUID): HeartbeatData? {
        return heartBeatData[uuid]
    }

    fun getAllLoaded(): Collection<HeartbeatData> {
        return heartBeatData.values
    }

    fun set(uuid: UUID, accessToken: UUID): HeartbeatData {
        if (heartBeatData.contains(uuid)) {
            remove(uuid)
        }

        val socket = flatline.client.newWebSocket(
            Request.Builder()
                .url("wss://dev.pulsoid.net/api/v1/data/real_time?access_token=${accessToken}&response_mode=legacy_json")
                .build(),
            PulsoidWebSocketListener(uuid)
        )

        val heartbeatData = HeartbeatData(uuid, accessToken, socket)
        heartBeatData[uuid] = heartbeatData

        return heartbeatData
    }

    fun save(uuid: UUID) {
        val data = heartBeatData[uuid]
            ?: return

        flatline.accessTokens.set(uuid.toString(), data.accessToken.toString())
    }

    fun remove(uuid: UUID) {
        val data = heartBeatData[uuid]
            ?: return

        data.socket.cancel()
        heartBeatData.remove(uuid)

        val player = flatline.server.getPlayer(uuid)
        if (player != null && data.bossbar != null) {
            player.hideBossBar(data.bossbar!!)
        }
    }

}
