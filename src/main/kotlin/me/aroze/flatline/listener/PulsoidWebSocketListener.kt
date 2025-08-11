import kotlinx.serialization.json.Json
import me.aroze.flatline.flatline
import me.aroze.flatline.model.pulsoid.PulsoidResponse
import me.aroze.flatline.registry.HeartbeatRegistry
import net.kyori.adventure.text.Component
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.bukkit.Bukkit
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
                }
            }
        } catch (e: Exception) {
            println("Parse error: ${e.message}")
        }
    }
}
