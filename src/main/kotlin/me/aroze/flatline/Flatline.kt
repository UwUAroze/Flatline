package me.aroze.flatline

import me.aroze.flatline.command.SetAccessTokenCommand
import me.aroze.flatline.command.ToggleCommand
import me.aroze.flatline.listener.bukkit.PlayerConnectionListener
import me.aroze.flatline.ticker.HeartbeatTicker
import net.kyori.adventure.text.minimessage.MiniMessage
import okhttp3.OkHttpClient
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager
import java.io.File
import kotlin.jvm.java

class Flatline : JavaPlugin() {

    val client = OkHttpClient()
    val mm = MiniMessage.miniMessage()

    lateinit var accessTokens: YamlConfiguration
        private set

    override fun onLoad() {
        accessTokens = YamlConfiguration.loadConfiguration(getAccessTokensFile())
    }

    override fun onEnable() {
        registerCommands()
        registerListeners()
        Bukkit.getScheduler().runTaskTimer(this, HeartbeatTicker(), 1L, 1L)
    }

    override fun onDisable() {
        accessTokens.save(getAccessTokensFile())
    }

    private fun getAccessTokensFile(): File {
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            logger.severe("Failed to create data folder: ${dataFolder.absolutePath}")
        }

        val file = File(dataFolder, "access-tokens.yml")
        if (!file.exists()) {
            file.createNewFile()
        }

        return file
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(PlayerConnectionListener, this)
    }

    private fun registerCommands() {
        val commandManager =
            LegacyPaperCommandManager(this, ExecutionCoordinator.simpleCoordinator(), SenderMapper.identity())

        val annotationParser =
            AnnotationParser(commandManager, CommandSender::class.java)

        annotationParser.parse(SetAccessTokenCommand)
        annotationParser.parse(ToggleCommand)
    }
}

val flatline: Flatline
    get() = JavaPlugin.getPlugin(Flatline::class.java)
