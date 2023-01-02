package de.elliepotato.commandalias.executor.context;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class ExecutionContextImpl implements ExecutionContext {

    public static ExecutionContextImpl newContext(
            CommandSender executor, List<CommandAlias> matchedAliases,
            String matchedLabel, List<String> args
    ) {
        return new ExecutionContextImpl(executor, matchedAliases, matchedLabel, args);
    }

    private final CommandSender sender;
    private final List<CommandAlias> aliases;
    private final String matchedLabel;
    private final List<String> args;

    public ExecutionContextImpl(
            CommandSender sender, List<CommandAlias> aliases,
            String matchedLabel, List<String> args
    ) {
        this.sender = sender;
        this.aliases = aliases;
        this.matchedLabel = matchedLabel;
        this.args = args;
    }

    @Override
    public String getExecutorId() {
        if (sender instanceof Player) {
            return ((Player) sender).getUniqueId().toString();
        } else {
            return sender.getName();
        }
    }

    @Override
    public CommandSender getExecutor() {
        return sender;
    }

    @Override
    public List<CommandAlias> getMatchedAliases() {
        return aliases;
    }

    @Override
    public String getMatchedLabel() {
        return matchedLabel;
    }

    @Override
    public List<String> getArgs() {
        return args;
    }
}
