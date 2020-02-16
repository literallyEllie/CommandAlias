package de.elliepotato.commandalias.bungee

import com.google.common.collect.Iterables
import com.google.common.collect.Sets
import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import de.elliepotato.commandalias.CommandAlias
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

/**
 * Created by Ellie on 31/12/2019 for CommandAlias.
 *
 *    Copyright 2019 Ellie
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
class BungeeConnector(private val plugin: CommandAlias) {

    private val pendingConnection: MutableSet<Player> = Sets.newHashSet()

    init {
        plugin.server.messenger.registerOutgoingPluginChannel(plugin, "BungeeCord")

        plugin.server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onQuit(e: PlayerQuitEvent) {
                pendingConnection.remove(e.player)
            }
        }, plugin)
    }

    fun sendServer(player: Player, server: String) {
        if (pendingConnection.contains(player))
            return
        if (constructMessage(player, "Connect", server))
            pendingConnection.add(player)
    }

    private fun constructMessage(player: Player? = Iterables.getFirst(plugin.server.onlinePlayers, null),
                                 subChannel: String, argument: String): Boolean {
        if (player == null)
            return false

        val out: ByteArrayDataOutput = ByteStreams.newDataOutput()
        out.writeUTF(subChannel)
        out.writeUTF(argument)

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray())
        return true
    }

}