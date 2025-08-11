package me.aroze.flatline.listener.bukkit

import me.aroze.flatline.registry.HeartRateRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerConnectionListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        HeartRateRegistry.getOrLoad(event.player.uniqueId)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        HeartRateRegistry.remove(event.player.uniqueId)
    }

}
