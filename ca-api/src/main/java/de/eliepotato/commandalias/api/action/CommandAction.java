package de.eliepotato.commandalias.api.action;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.util.List;

public class CommandAction implements AliasAction {

    public static CommandAction of(String... commands) {
        return new CommandAction(List.of(commands));
    }

    private List<String> commands;

    public CommandAction(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public boolean run(CommandAlias alias, ExecutionContext context) {
        // TODO placeholders

        ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
        for (String command : commands) {
            Bukkit.dispatchCommand(
                    consoleSender,
                    command
            );
        }

        return true;
    }
}
