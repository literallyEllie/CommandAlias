package de.elliepotato.commandalias

import de.elliepotato.commandalias.backend.AliasCommand
import de.elliepotato.commandalias.backend.AliasConfig
import de.elliepotato.commandalias.backend.CommandType
import de.elliepotato.commandalias.command.CmdHandle
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

/**
 * Created by Ellie on 27/07/2017 for PublicPlugins.
 *
 *    Copyright 2017 Ellie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
class CommandAlias: JavaPlugin() {

    lateinit var config: AliasConfig

    lateinit var newCommands: HashMap<String, AliasCommand>

    lateinit var prefix: String
    lateinit var noPermission: String
    var error: String? = null

    override fun onEnable() {

        if (!dataFolder.exists()) dataFolder.mkdirs()

        config = AliasConfig(this, dataFolder)
        newCommands = config.getNewCommands()
        prefix = config.getPrefix()
        noPermission = config.getNoPerm()

        getCommand("ca").executor = CmdHandle(this)

        Bukkit.getPluginManager().registerEvents(object : Listener {

            @EventHandler(priority = EventPriority.LOW)
            fun onCommandPreProcess(e: PlayerCommandPreprocessEvent) {
                val player: Player = e.player
                val message: String = e.message.replaceFirst("/", "")

                val argOne = message.split(" ")[0]
                val commandArgs = message.substring(argOne.length)

                for ((key, toEx) in newCommands) {
                    if (!toEx.enabled || (key != argOne && !toEx.aliases.contains(argOne))) continue
                    e.isCancelled = true
                    if (toEx.permission.isNullOrEmpty() || player.hasPermission(toEx.permission)) {

                        when(toEx.type) {
                            CommandType.MSG -> player.sendMessage(color(toEx.aliases[0]))
                            CommandType.CMD -> server.dispatchCommand(player, "${toEx.label}$commandArgs") // kotlin lost it when i used a + operator??
                        }

                    } else player.sendMessage(color(noPermission))
                    break
                }
            }

        }, this)

        Metrics(this)

        log("${newCommands.size} command aliases were loaded!")
        log("CommandAlias V.${description.version}, by Ellie, has been enabled!")
    }

    override fun onDisable() {
        newCommands.clear()
        log("CommandAlias V.${description.version}, by Ellie, has been disabled!")
    }

    fun log(message: String, level: Level = Level.INFO) = logger.log(level, message)

    fun reload() {
        error = null
        config.reload()
        newCommands = config.getNewCommands()
        log("${newCommands.size} command aliases were loaded!")
    }

    fun color(msg: String): String = ChatColor.translateAlternateColorCodes('&', msg)

}