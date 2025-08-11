package me.aroze.flatline.command

import me.aroze.flatline.flatline
import me.aroze.flatline.registry.HeartRateRegistry
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command

object ToggleCommand {

    @Command("flatline toggle")
    fun toggle(player: Player) {
        val cachedHeartbeatData = HeartRateRegistry.getLoaded(player.uniqueId)

        if (cachedHeartbeatData != null) {
            HeartRateRegistry.remove(player.uniqueId)
            player.sendMessage(flatline.mm.deserialize("<#ffbfbf>Heart rate tracking has been disabled ;c"))
        } else {
            player.sendMessage(flatline.mm.deserialize("<#bfffd1>Heart rate tracking has been enabled!"))
            HeartRateRegistry.getOrLoad(player.uniqueId)
        }
    }

}
