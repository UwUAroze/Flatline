import kotlinx.serialization.json.Json
import me.aroze.flatline.flatline
import me.aroze.flatline.model.HeartbeatData
import me.aroze.flatline.model.pulsoid.PulsoidResponse
import me.aroze.flatline.registry.HeartbeatRegistry
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

class PulsoidWebSocketListener(val uuid: UUID) : WebSocketListener() {
    private val json = Json { ignoreUnknownKeys = true }
    private var lastHeartRate: Int? = null

    override fun onOpen(webSocket: WebSocket, response: Response) {
        val player = Bukkit.getPlayer(uuid) ?: return

        player.sendMessage(flatline.mm.deserialize("<#bfffd1>Your heartbeat is now being tracked <3"))

        HeartbeatRegistry.save(player.uniqueId)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val hearbeatData = HeartbeatRegistry.getOrLoad(uuid)
            ?: return

        try {
            val hrData = json.decodeFromString<PulsoidResponse>(text)
            if (hrData.data.heartRate != lastHeartRate) {
                lastHeartRate = hrData.data.heartRate
                hearbeatData.bpm = hrData.data.heartRate

                val player = Bukkit.getPlayer(uuid)
                if (player != null) {
                    Bukkit.broadcast(Component.text("${player.name}: ${hrData.data.heartRate} BPM"))
                    updateBossBar(player, hearbeatData)
                }
            }
        } catch (e: Exception) {
            println("Parse error: ${e.message}")
        }
    }

    private fun updateBossBar(player: Player, data: HeartbeatData) {
        val max = 175f
        val min = 30f

        val bossBar: BossBar = data.bossbar ?: BossBar.bossBar(
            flatline.mm.deserialize("um!"),
            0f,
            BossBar.Color.GREEN,
            BossBar.Overlay.NOTCHED_6
        )

        val filled = (data.bpm!! - min) / (max - min)
        bossBar.name(flatline.mm.deserialize("<#bfffd1>${data.bpm} BPM"))
        bossBar.progress(filled.coerceIn(0f, 1f))

        if (data.bossbar == null) {
            player.showBossBar(bossBar)
            data.bossbar = bossBar
        }

    }
}
