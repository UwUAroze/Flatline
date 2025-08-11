package me.aroze.flatline.ticker

import me.aroze.flatline.registry.HeartRateRegistry
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

class HeartbeatTicker : Runnable {

    var tick = 0

    override fun run() {
        tick++

        for (data in HeartRateRegistry.getAllLoaded()) {
            if (data.bpm == null) {
                continue
            }

            val player = Bukkit.getPlayer(data.uuid) ?: continue
            val ticksPerBeat = (1200.0 / data.bpm!!).toInt()

            if (tick - data.lastHeartbeatTick >= ticksPerBeat) {
                playHeartbeatSound(player)
                data.lastHeartbeatTick = tick
            }
        }

    }

    private fun playHeartbeatSound(player: Player) {
        player.playSound(player, Sound.ENTITY_WARDEN_HEARTBEAT, 1f, 1f)
    }
}
