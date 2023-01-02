package de.elliepotato.commandalias.listener;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;
import de.elliepotato.commandalias.CommandAliasPlugin;
import de.elliepotato.commandalias.executor.context.ExecutionContextImpl;
import de.elliepotato.commandalias.matcher.AliasMatcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.Arrays;
import java.util.List;

public final class CommandListener implements Listener {

    private final CommandAliasPlugin plugin;
    private final AliasMatcher matcher;

    public CommandListener(CommandAliasPlugin plugin) {
        this.plugin = plugin;
        this.matcher = AliasMatcher.newMatcher(plugin.getRegistry());
    }

    @EventHandler
    public void onCommandSent(PlayerCommandSendEvent event) {
        // add our things???
    }

    // todo send other commands with PlayerCommandSendEvent ?

    @EventHandler(priority = EventPriority.LOW)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player executor = event.getPlayer();
        String message = event.getMessage().replaceFirst("/", "");

        List<CommandAlias> matches = matcher.match(message);
        if (matches.isEmpty())
            return;

        List<String> fullCommand = Arrays.stream(message.split(" ")).toList();
        String matchedLabel = fullCommand.get(0);
        List<String> args = fullCommand.size() == 1 ? List.of() : fullCommand.subList(1, fullCommand.size());

        ExecutionContext context = ExecutionContextImpl.newContext(
                executor, matches, matchedLabel, args
        );

        boolean success = plugin.getExecutor().execute(
                matches, context
        );

        if (success) {
            // TODO call some event?
        }
    }

}
