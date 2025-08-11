package me.aroze.flatline.command

import me.aroze.flatline.flatline
import me.aroze.flatline.registry.HeartbeatRegistry
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command
import java.util.UUID

object SetAccessTokenCommand {

    @Command("flatline set-access-token <token>")
    fun setAccessToken(player: Player, token: UUID) {
        HeartbeatRegistry.set(player.uniqueId, token)
    }

}
