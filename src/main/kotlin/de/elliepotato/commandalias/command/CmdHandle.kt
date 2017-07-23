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
class CmdHandle(val core: CommandAlias): CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        // Commands: /ca reload / toggle

        if (!sender.hasPermission("commandalias.reload")) {
            msg(sender, "No permission.")
            return true
        }

        if (args.isEmpty()) {
            msg(sender, correctUsage())
            return true
        }

        when (args[0].toLowerCase()) {
            "reload" -> handleReload(sender)
            "toggle" -> handleToggle(sender, args)
        }

        return true
    }

    fun handleReload(sender: CommandSender) {
        core.reload()
        msg(sender, "Reloaded.")
    }

    fun handleToggle(sender: CommandSender, args: Array<out String>){
        if(args.size != 2) {
            msg(sender, correctUsage())
        }
        core.commands = core.config.toggleAlias(core.commands, args[1])
        msg(sender, "Toggled '${args[1]}'.")
    }

    private fun msg(sender: CommandSender, msg: String) {
        sender.sendMessage(color(core.prefix + msg))
    }

    private fun color(msg: String): String {
        return ChatColor.translateAlternateColorCodes('&', msg)
    }

    private fun correctUsage(): String {
        return color("Correct usage: &7/ca <reload | toggle <label>>")
    }


}