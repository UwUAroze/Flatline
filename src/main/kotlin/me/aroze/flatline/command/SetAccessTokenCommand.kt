package me.aroze.flatline.command

import me.aroze.flatline.registry.HeartRateRegistry
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command
import java.util.UUID

object SetAccessTokenCommand {

    @Command("flatline set-access-token <token>")
    fun setAccessToken(player: Player, token: UUID) {
        HeartRateRegistry.set(player.uniqueId, token)
    }

}
