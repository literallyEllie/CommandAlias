package de.eliepotato.commandalias.api.alias.builder;

import de.eliepotato.commandalias.api.alias.AliasType;
import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;
import de.eliepotato.commandalias.api.hook.PostprocessorHook;
import de.eliepotato.commandalias.api.hook.AliasRunCondition;
import de.eliepotato.commandalias.api.util.AliasPriority;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class BuiltCommandAlias implements CommandAlias {
    private final String label;
    private final Set<String> aliases;
    private boolean enabled;
    private AliasType type;
    private AliasPriority matchPriority;
    private final Function<ExecutionContext, Boolean> aliasFunction;
    private final List<AliasRunCondition> aliasRunConditions;
    private final List<PostprocessorHook> postprocessorHooks;
    private final Map<String, Boolean> settings;

    public BuiltCommandAlias(
            String label, Set<String> aliases,
            @Nullable Function<ExecutionContext, Boolean> aliasFunction,
            List<AliasRunCondition> aliasRunConditions,
            List<PostprocessorHook> postprocessorHooks,
            Map<String, Boolean> settings
    ) {
        this.label = label;
        this.aliases = aliases;
        this.aliasFunction = aliasFunction;
        this.aliasRunConditions = aliasRunConditions;
        this.postprocessorHooks = postprocessorHooks;
        this.settings = settings;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Set<String> getAliases() {
        return aliases;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public AliasType getType() {
        return type;
    }

    @Override
    public void setType(AliasType newType) {
        this.type = newType;
    }

    @Override
    public AliasPriority getMatchPriority() {
        return matchPriority;
    }

    @Override
    public void setMatchPriority(AliasPriority newPriority) {
        this.matchPriority = newPriority;
    }

    @Override
    public List<AliasRunCondition> getPreprocessorHooks() {
        return aliasRunConditions;
    }

    @Override
    public List<PostprocessorHook> getPostprocessorHooks() {
        return postprocessorHooks;
    }

    @Override
    public boolean execute(ExecutionContext context) {
        if (aliasFunction != null) {
            return aliasFunction.apply(context);
        }

        return true;
    }

    @Override
    public Map<String, Boolean> getSettings() {
        return settings;
    }
}
