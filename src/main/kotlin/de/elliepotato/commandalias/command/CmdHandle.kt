package de.elliepotato.commandalias.command

import de.elliepotato.commandalias.CommandAlias
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * Created by Ellie on 23.7.17 for PublicPlugins.
 * Affiliated with www.elliepotato.de
 *
 */
class CmdHandle(private val core: CommandAlias): CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (!core.error.isNullOrEmpty()) msg(sender, "${ChatColor.RED}Warning! The plugin has detected an error on start up! Check console. Error description: ${core.error}")

        if (args.isEmpty()) {
            msg(sender, correctUsage())
            return true
        }

        when (args[0].toLowerCase()) {
            "reload" -> handleReload(sender)
            "toggle" -> handleToggle(sender, args)
            else -> msg(sender, correctUsage())
        }

        return true
    }

    private fun handleReload(sender: CommandSender) {
        if (!sender.hasPermission("commandalias.reload")) {
            msg(sender, core.noPermission)
            return
        }
        core.reload()
        if (!core.error.isNullOrEmpty()) msg(sender, "${ChatColor.RED}Warning! The plugin has detected an error whilst reloading! Check console. Error description: ${core.error}")
        msg(sender, "Reloaded.")
    }

    private fun handleToggle(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("commandalias.toggle")) {
            msg(sender, core.noPermission)
            return
        }

        if (!core.error.isNullOrEmpty()) {
            msg(sender, "${ChatColor.RED}Warning! The plugin has detected an error on start up so " +
                    "this sub-command cannot be executed! Check console. Error description: ${core.error}")
            return // soz
        }

        if (args.size != 2) {
            msg(sender, correctUsage())
            return
        }
        val label = args[1]

        val rMap = core.config.toggleAlias(core.newCommands, label)
        if (!rMap.second) msg(sender, "Couldn't find value of '$label'.")
        else {
            core.newCommands = rMap.first
            msg(sender, "Toggled '$label'.")
        }
    }

    private fun msg(sender: CommandSender, msg: String) = sender.sendMessage(core.color(core.prefix + msg))

    private fun correctUsage(): String = "Correct usage: ${ChatColor.GRAY}/ca <reload | toggle <label>>"

}