package de.elliepotato.commandalias.executor;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;
import de.eliepotato.commandalias.api.executor.AliasExecutor;
import de.eliepotato.commandalias.api.hook.ExecutionHook;
import de.eliepotato.commandalias.api.hook.PostprocessorHook;
import de.eliepotato.commandalias.api.hook.AliasRunCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AliasExecutorImpl implements AliasExecutor {
    private final Logger logger = LoggerFactory.getLogger(AliasExecutorImpl.class);

    private final List<AliasRunCondition> globalAliasRunConditions = new ArrayList<>();
    private final List<PostprocessorHook> globalPostprocessorHooks = new ArrayList<>();

    @Override
    public boolean execute(CommandAlias alias, ExecutionContext context) {
        logger.debug(
                "Executing alias {} for {} (args: {})",
                alias.getLabel(), context.getExecutorId(), context.getArgs()
        );

        // First, run pre-processors
        logger.debug("Running global preprocessor hooks for {}", alias.getLabel());
        boolean allow = runPreprocessorHooks(alias, context, globalAliasRunConditions);
        if (!allow) {
            // TODO anything else?
            return false;
        }

        List<AliasRunCondition> aliasRunConditions = alias.getRunConditions();
        logger.debug(
                "Alias {} has {} pre-processor hooks",
                alias.getLabel(), aliasRunConditions.size()
        );
        allow = runPreprocessorHooks(alias, context, aliasRunConditions);
        if (!allow) {
            // TODO anything else?
            return false;
        }

        // Ok lets execute it.
        logger.debug(
                "No interruptions from preprocessors, executing..."
        );
        boolean success = false;
        try {
            success = alias.execute(context);
            logger.debug("Alias execution run successfully, running postprocessors");
        } catch (Throwable e) {
            logger.error(
                    "Failed to execute alias {} for {} (args: {})",
                    alias.getLabel(), context.getExecutorId(), context.getArgs()
            );
            success = false;
        } finally {
            // Then post processors
            List<PostprocessorHook> postProcessorHooks = alias.getPostprocessorHooks();
            logger.debug(
                    "Alias {} has {} post-processor hooks",
                    alias.getLabel(), postProcessorHooks.size()
            );
            runPostprocessorHooks(alias, context, success, postProcessorHooks);

            logger.debug("Running global preprocessor hooks for {}", alias.getLabel());
            runPostprocessorHooks(alias, context, success, globalPostprocessorHooks);
        }

        logger.debug("Execution of {} for {} successful", alias.getLabel(), context.getExecutorId());
        return true;
    }

    @Override
    public boolean execute(Collection<CommandAlias> aliases, ExecutionContext context) {

        boolean success = false;
        for (CommandAlias alias : aliases) {
            if (!alias.isEnabled())
                continue;

            success = execute(alias, context);
            if (success && alias.getSetting("breakAfterRun", true)) {
                break;
            }
        }

        return success;
    }

    @Override
    public void registerGlobalExecutionHook(ExecutionHook hook) {
        if (hook instanceof AliasRunCondition aliasRunCondition) {
            globalAliasRunConditions.add(aliasRunCondition);
            logger.debug("Registered preprocessor hook {}", hook.getClass().getName());
        } else if (hook instanceof PostprocessorHook postprocessorHook) {
            globalPostprocessorHooks.add(postprocessorHook);
            logger.debug("Registered postprocessor hook {}", hook.getClass().getName());
        } else {
            throw new IllegalArgumentException(String.format(
                            "execution hook %s not support for default AliasExecutor", hook.getClass().getName()
                    ));
        }
    }

    private boolean runPreprocessorHooks(CommandAlias alias, ExecutionContext context, List<AliasRunCondition> hooks) {
        for (AliasRunCondition aliasRunCondition : hooks) {
            logger.debug(
                    "Running preprocessor hook {} for {}",
                    aliasRunCondition.getClass().getName(), alias.getLabel()
            );

            if (!aliasRunCondition.shouldRun(alias, context)) {
                logger.debug(
                        "Preprocessor {} cancelled exception for {}", alias.getLabel(),
                        aliasRunCondition.getClass().getName());
                return false;
            }
        }

        return true;
    }

    private void runPostprocessorHooks(CommandAlias alias, ExecutionContext context, boolean success, List<PostprocessorHook> hooks) {
        for (PostprocessorHook preprocessorHook : hooks) {
            logger.debug(
                    "Running postprocessor hook {} for {}",
                    preprocessorHook.getClass().getName(), alias.getLabel()
            );

            if (success) {
                preprocessorHook.onSuccess(alias, context);
            } else {
                preprocessorHook.onFail(alias, context);
            }
        }
    }

}
