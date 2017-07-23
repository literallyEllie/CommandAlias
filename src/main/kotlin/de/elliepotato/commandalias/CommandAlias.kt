package de.elliepotato.commandalias

import de.elliepotato.commandalias.backend.AliasConfig
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.logging.Level



/**
 * Created by Ellie on 23.7.17 for PublicPlugins.
 * Affiliated with www.elliepotato.de
 *
 */
class CommandAlias: JavaPlugin() {

    lateinit var config: AliasConfig
    lateinit var commands: HashMap<String, String>

    /*
    When player exectues
     */

    override fun onEnable() {

        if(!dataFolder.exists()) dataFolder.mkdirs()

        config = AliasConfig(dataFolder)
        commands = config.getCommands()

        Bukkit.getPluginManager().registerEvents(object: Listener {

            @EventHandler
            fun onCommandPreProcess(e: PlayerCommandPreprocessEvent){
                val player: Player = e.player
                val message: String = e.message.replaceFirst("/", "")

                val argOne = message.split(" ")[0]
                val commandArgs = message.substring(argOne.length)

                for ((key, toEx) in commands) {
                    if (argOne.toLowerCase() == key) {
                        e.isCancelled = true
                        server.dispatchCommand(player, toEx+commandArgs) // This avoids dupe command registers in console +
                                                                        // potential chat filters (cmd spam)
                        break // Stop command dupes
                    }
                }
            }

        }, this)

        log("${commands.size} command aliases were loaded!")
        log("CommandAlias V.${description.version}, by Ellie, has been enabled!")
    }

    override fun onDisable() {
        commands.clear()
        log("CommandAlias V.${description.version}, by Ellie, has been disabled!")
    }

    fun log(message: String, level:Level = Level.INFO) = logger.log(level, message)
}