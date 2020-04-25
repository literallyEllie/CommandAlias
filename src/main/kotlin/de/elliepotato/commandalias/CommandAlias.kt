package de.elliepotato.commandalias

import com.google.common.collect.Maps
import com.google.common.collect.Sets
import de.elliepotato.commandalias.backend.AliasCommand
import de.elliepotato.commandalias.backend.AliasConfig
import de.elliepotato.commandalias.backend.CommandType
import de.elliepotato.commandalias.backend.prerun.RunCondition
import de.elliepotato.commandalias.backend.prerun.impl.RunConditionArgs
import de.elliepotato.commandalias.backend.prerun.impl.RunConditionGamemode
import de.elliepotato.commandalias.backend.prerun.impl.RunConditionHealth
import de.elliepotato.commandalias.backend.prerun.impl.RunConditionWorld
import de.elliepotato.commandalias.bungee.BungeeConnector
import de.elliepotato.commandalias.command.CommandHandle
import de.elliepotato.commandalias.event.CAPluginReloadEvent
import de.elliepotato.commandalias.hook.CAHook
import de.elliepotato.commandalias.hook.DefaultPlaceholders
import de.elliepotato.commandalias.hook.HookPlaceholderAPI
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.text.MessageFormat
import java.util.logging.Level

/**
 * Created by Ellie on 27/07/2017 for CommandAlias.
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
class CommandAlias : JavaPlugin(), CommandAliasAPI {

    lateinit var config: AliasConfig

    private lateinit var commands: MutableMap<String, AliasCommand>
    private lateinit var hookProcessors: MutableSet<CAHook>
    private lateinit var runConditions: MutableMap<String, RunCondition>
    lateinit var bungeeConnector: BungeeConnector

    lateinit var prefix: String
    lateinit var noPermission: String
    var error: String? = null

    var updateMeMessage: String? = null
        set(value) {
            field = "$value You can download a new version at https://www.spigotmc.org/resources/commandalias.44362/"
        }

    override fun onEnable() {
        // setup plugin
        config = AliasConfig(this)
        hookProcessors = Sets.newHashSet()
        runConditions = Maps.newHashMap()
        bungeeConnector = BungeeConnector(this)

        // load values into memory
        reload()

        // register command
        getCommand("ca")!!.setExecutor(CommandHandle(this))

        // register listeners
        Bukkit.getPluginManager().registerEvents(object : Listener {

            @EventHandler
            fun onJoin(event: PlayerJoinEvent) {
                if (!config.isVersionChecking() || updateMeMessage == null)
                    return

                val player = event.player
                if (!player.hasPermission("commandalias.reload"))
                    return

                player.sendMessage("${config.getPrefix()} $updateMeMessage")
            }

            @EventHandler(priority = EventPriority.LOW)
            fun onCommand(event: PlayerCommandPreprocessEvent) {
                val player: Player = event.player
                val message: String = event.message.replaceFirst("/", "")

                val args = message.split(" ");
                val argOne = args[0]

                for ((key, toEx) in commands) {
                    // Check enabled and either it is equal to the label or the alias contains it
                    if (!toEx.enabled || (key != argOne && !toEx.aliases.contains(argOne)))
                        continue

                    // check run condition, for each run condition it has, if one is not met break.
                    if (toEx.runConditions
                                    .filter { entry -> runConditions.containsKey(entry.key) }
                                    .any { entry ->
                                        try {
                                            !runConditions[entry.key]!!.meetsConditions(toEx, args, player)
                                        } catch (ex: Exception) {
                                            log("The run condition ${entry.key} threw an unexpected error whilst processing", Level.SEVERE)
                                            ex.printStackTrace()
                                            false
                                        }
                                    })
                        break

                    event.isCancelled = true
                    // if no perm OR they have the perm
                    if (toEx.permission.isNullOrEmpty() || player.hasPermission(toEx.permission)) {
                        val formatMessage = toEx.insertPlaceholders(args)

                        // response
                        when (toEx.type) {
                            CommandType.MSG -> toEx.aliases.forEach { m -> player.sendMessage(processString(m, player)) }
                            CommandType.SERVER -> bungeeConnector.sendServer(player, key)
                            CommandType.CMD -> server.dispatchCommand(player, processString(formatMessage, player))
                        }

                        toEx.consoleCommands.forEach { command ->
                            server.dispatchCommand(server.consoleSender,
                                    processString(MessageFormat.format(command, *args.toTypedArray()), player)
                                            .replace("%alias%", argOne))
                        }
                    } else if (noPermission.isNotEmpty()) {
                        // if let run if no permission, uncancel + disregard
                        if (config.isLetCmdRunIfNoPerm()) {
                            event.isCancelled = false
                            break
                        }

                        player.sendMessage(noPermission)
                    }

                    if (config.isBreakAfterAliasMatch())
                        break
                }
            }

        }, this)

        // Start metrics
        Metrics(this, 1242)

        log("CommandAlias V.${description.version}, by Ellie#0006, has been enabled!")
    }

    override fun onDisable() {
        commands.clear()
        hookProcessors.clear()
        runConditions.clear()

        log("CommandAlias V.${description.version}, by Ellie#0006, has been disabled!")
    }

    fun log(message: String, level: Level = Level.INFO) = logger.log(level, message)

    /**
     * Reloads the plugin completely.
     */
    fun reload() {
        error = null
        // load config stuff
        config.reloadFile()
        commands = config.getCommands()
        prefix = config.getPrefix()
        noPermission = config.getNoPerm()

        // reg hook processor
        hookProcessors.clear()
        registerInternalHookProcessors()
        // reg run condition
        runConditions.clear()
        registerInternalRunConditionTypes()

        // call event
        server.pluginManager.callEvent(CAPluginReloadEvent(this))
        log("${commands.size} command aliases were loaded!")
    }

    /* API Methods */

    override fun registerAlias(aliasCommand: AliasCommand) {
        this.commands[aliasCommand.label.toLowerCase()] = aliasCommand
    }

    override fun unregisterAlias(label: String) {
        this.commands.remove(label.toLowerCase())
    }

    override fun toggleAlias(label: String): Boolean {
        val response = config.toggleAlias(commands, label)
        if (response.second) {
            this.commands = response.first
        }
        return response.second
    }

    override fun registerRunCondition(runCondition: RunCondition) {
        this.runConditions[runCondition.getId().toLowerCase()] = runCondition
    }

    override fun unregisterRunCondition(id: String) {
        this.runConditions.remove(id.toLowerCase())
    }

    override fun registerPlaceholderHook(hook: CAHook) {
        hookProcessors.add(hook)
    }

    override fun unregisterPlaceholderHook(hook: CAHook) {
        hookProcessors.remove(hook)
    }

    override fun processString(message: String, sender: Player): String {
        var newMsg = message

        hookProcessors.forEach { processor ->
            try {
                newMsg = processor.process(newMsg, sender)
            } catch (ex: Exception) {
                log("The placeholder processor ${processor.javaClass.simpleName} threw an unexpected error whilst processing")
                ex.printStackTrace()
            }
        }

        return newMsg
    }

    /**
     * Registers soft dependency handlers.
     */
    private fun registerInternalHookProcessors() {
        if (server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            log("PlaceholderAPI found! Utilizing...")
            registerPlaceholderHook(HookPlaceholderAPI())
        }

        // %name% and %display_name%
        registerPlaceholderHook(DefaultPlaceholders())
    }

    /**
     * Register internal run conditions for the aliases
     */
    private fun registerInternalRunConditionTypes() {
        // Args
        registerRunCondition(RunConditionWorld())
        // World
        registerRunCondition(RunConditionArgs())
        // Gamemode
        registerRunCondition(RunConditionGamemode())
        // Healh
        registerRunCondition(RunConditionHealth())
    }

}