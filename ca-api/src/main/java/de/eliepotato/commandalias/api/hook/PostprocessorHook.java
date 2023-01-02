package de.eliepotato.commandalias.api.hook;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;

public interface PostprocessorHook extends ExecutionHook {
    // todo is this even useful idk - also this is more of a consumer

    void onSuccess(CommandAlias command, ExecutionContext context);

    void onFail(CommandAlias command, ExecutionContext context);

}
