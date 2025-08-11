package me.aroze.flatline.command

import me.aroze.flatline.flatline
import me.aroze.flatline.registry.HeartbeatRegistry
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command

object ToggleCommand {

    @Command("flatline toggle")
    fun toggle(player: Player) {
        val cachedHeartbeatData = HeartbeatRegistry.getLoaded(player.uniqueId)

        if (cachedHeartbeatData != null) {
            HeartbeatRegistry.remove(player.uniqueId)
            player.sendMessage(flatline.mm.deserialize("<#ffbfbf>Heartbeat tracking has been disabled ;c"))
        } else {
            player.sendMessage(flatline.mm.deserialize("<#bfffd1>Heartbeat tracking has been enabled!"))
            HeartbeatRegistry.getOrLoad(player.uniqueId)
        }
    }

}
