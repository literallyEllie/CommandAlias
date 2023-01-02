package de.eliepotato.commandalias.api.hook;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;
import de.eliepotato.commandalias.api.util.AliasPriority;

public interface AliasRunCondition extends ExecutionHook {

    @Override
    default AliasPriority getPriority() {
        return AliasPriority.NORMAL;
    }

    boolean shouldRun(CommandAlias command, ExecutionContext context);

}
