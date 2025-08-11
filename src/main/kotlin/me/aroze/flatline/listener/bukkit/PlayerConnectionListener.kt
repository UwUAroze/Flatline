package me.aroze.flatline.listener.bukkit

import me.aroze.flatline.registry.HeartbeatRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerConnectionListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        HeartbeatRegistry.getOrLoad(event.player.uniqueId)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        HeartbeatRegistry.remove(event.player.uniqueId)
    }

}
