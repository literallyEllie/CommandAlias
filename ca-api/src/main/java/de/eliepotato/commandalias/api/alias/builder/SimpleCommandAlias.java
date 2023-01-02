package de.eliepotato.commandalias.api.alias.builder;

import de.eliepotato.commandalias.api.action.AliasAction;
import de.eliepotato.commandalias.api.alias.AliasType;
import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;
import de.eliepotato.commandalias.api.hook.PostprocessorHook;
import de.eliepotato.commandalias.api.hook.AliasRunCondition;
import de.eliepotato.commandalias.api.util.AliasPriority;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple command alias implementation for the lads.
 */
public final class SimpleCommandAlias implements CommandAlias {
    private final String label;
    private final Set<String> aliases;
    private final List<AliasAction> actions;
    private boolean enabled;
    private AliasType type;
    private AliasPriority matchPriority;
    private final List<AliasRunCondition> aliasRunConditions;
    private final List<PostprocessorHook> postprocessorHooks;
    private final Map<String, Boolean> settings;

    public SimpleCommandAlias(
            String label, Set<String> aliases, List<AliasAction> actions,
            List<AliasRunCondition> aliasRunConditions,
            List<PostprocessorHook> postprocessorHooks,
            Map<String, Boolean> settings
    ) {
        this.label = label;
        this.aliases = aliases;
        this.actions = actions;
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
    public List<AliasAction> getActions() {
        return actions;
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
    public List<AliasRunCondition> getRunConditions() {
        return aliasRunConditions;
    }

    @Override
    public List<PostprocessorHook> getPostprocessorHooks() {
        return postprocessorHooks;
    }

    @Override
    public boolean execute(ExecutionContext context) {
        boolean success = false;

        for (AliasAction action : actions) {
            success = action.run(this, context);
        }

        return success;
    }

    @Override
    public Map<String, Boolean> getSettings() {
        return settings;
    }
}
