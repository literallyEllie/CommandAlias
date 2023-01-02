package de.eliepotato.commandalias.api.action;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerChatAction implements AliasAction {

    public static PlayerChatAction of(String... messages) {
        return new PlayerChatAction(List.of(messages));
    }

    private List<String> messages;

    public PlayerChatAction(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public boolean run(CommandAlias alias, ExecutionContext context) {
        if (!(context.getExecutor() instanceof Player player))
            return true;

        // TODO placeholders

        for (String message : messages) {
            player.chat(message);
        }

        return true;
    }
}
