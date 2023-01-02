package de.eliepotato.commandalias.api.executor.context;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface ExecutionContext {

    /**
     * @return The ID of who is executing.
     */
    String getExecutorId();

    /**
     * @return Bukkit Command Sender of who is executing
     */
    CommandSender getExecutor();

    /**
     * @return All the aliases which match this label.
     */
    List<CommandAlias> getMatchedAliases();

    /**
     * @return The label inserted.
     */
    String getMatchedLabel();

    /**
     * @return Supplied additional arguments, excluding the original command.
     */
    List<String> getArgs();

}
