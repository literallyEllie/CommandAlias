package de.eliepotato.commandalias.api.hook.condition;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;
import de.eliepotato.commandalias.api.hook.AliasRunCondition;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

/**
 * A preprocessor hook player conditional.
 */
public interface PlayerCondition extends AliasRunCondition, Predicate<Player> {

    @Override
    default boolean shouldRun(CommandAlias command, ExecutionContext context) {
        if (context.getExecutor() instanceof Player player) {
            return test(player);
        }

        return true;
    }
}
