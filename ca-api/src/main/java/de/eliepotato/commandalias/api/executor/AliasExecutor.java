package de.eliepotato.commandalias.api.executor;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;
import de.eliepotato.commandalias.api.hook.AliasRunCondition;
import de.eliepotato.commandalias.api.hook.ExecutionHook;

import java.util.Collection;

/**
 * The thing that executes calls to a command alias with a paired execution context.
 * </p>
 * Global execution hooks can be registered which are called first and last
 * for each alias execution.
 */
public interface AliasExecutor {

    /**
     * Execute a command context to a context.
     * </p>
     * Execution could be cancelled by a {@link AliasRunCondition},
     * otherwise it will be the response of {@link CommandAlias#execute(ExecutionContext)}
     *
     * @param alias Alias to execute.
     * @param context Context for this execution.
     * @return If execution was successful or not.
     */
    boolean execute(CommandAlias alias, ExecutionContext context);

    /**
     * Execute for a collection of {@link CommandAlias}es
     * </p>
     * If the alias setting breakAfterRun is true, further aliases will not run.
     *
     * @param aliases Sorted collection of aliases to run.
     * @param context Context of the execution.
     * @return If all were successful in running.
     */
    boolean execute(Collection<CommandAlias> aliases, ExecutionContext context);

    /**
     * Register a global execution hook.
     * </p>
     * By default, only supports {@link AliasRunCondition}
     * and {@link de.eliepotato.commandalias.api.hook.PostprocessorHook} implementations.
     * </p>
     * They are called before executions and very last, respectively.
     *
     * @param hook The hook to register.
     */
    void registerGlobalExecutionHook(ExecutionHook hook);

}
