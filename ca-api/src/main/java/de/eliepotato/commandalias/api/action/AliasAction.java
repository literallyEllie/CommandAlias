package de.eliepotato.commandalias.api.action;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;

public interface AliasAction {

    boolean run(CommandAlias alias, ExecutionContext context);

}
