package de.eliepotato.commandalias.api.alias.builder;

import de.eliepotato.commandalias.api.action.AliasAction;
import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.util.AliasPriority;
import de.eliepotato.commandalias.api.hook.PostprocessorHook;
import de.eliepotato.commandalias.api.hook.AliasRunCondition;

import java.util.*;

/**
 * Builder to create a simple command alias.
 */
public final class AliasBuilder {

    public static AliasBuilder newBuilder() {
        return new AliasBuilder();
    }

    private String label;
    private final Set<String> aliases;
    private final List<AliasAction> actions;
    private AliasPriority matchPriority;
    private final List<AliasRunCondition> aliasRunConditions;
    private final List<PostprocessorHook> postprocessorHooks;
    private final Map<String, Boolean> settings;

    // TODO regex would be cool too

    private AliasBuilder() {
        this.aliases = new HashSet<>();
        this.actions = new LinkedList<>();
        this.aliasRunConditions = new ArrayList<>();
        this.postprocessorHooks = new ArrayList<>();
        this.settings = new HashMap<>();
    }

    /**
     * Set the main command label which is to be
     * executed.
     *
     * @param label Label to run when an alias is triggered.
     * @return Builder instance.
     */
    public AliasBuilder label(String label) {
        this.label = label;
        return this;
    }

    /**
     * Set the aliases that can be run
     * in alternative to the command label.
     *
     * @param aliases Aliases to run.
     * @return Builder instance.
     */
    public AliasBuilder aliases(String... aliases) {
        Collections.addAll(this.aliases, aliases);
        return this;
    }

    /**
     * Another action to run in additional to this.
     *
     * @param action Action to run.
     * @return Builder instance.
     */
    public AliasBuilder andDoAction(AliasAction action) {
        this.actions.add(action);
        return this;
    }

    /**
     * Set the match priority for this alias.
     * </p>
     * This can be significant if there are contending
     * aliases to a single label.
     *
     * @param matchPriority Priority to set.
     * @return Builder instance.
     */
    public AliasBuilder matchPriority(AliasPriority matchPriority) {
        this.matchPriority = matchPriority;
        return this;
    }

    public AliasBuilder addPreprocessorHook(AliasRunCondition aliasRunCondition) {
        this.aliasRunConditions.add(aliasRunCondition);
        return this;
    }

    public AliasBuilder addPostprocessorHook(PostprocessorHook postprocessorHook) {
        this.postprocessorHooks.add(postprocessorHook);
        return this;
    }

    /**
     * Set a setting.
     *
     * @param key Key to set.
     * @param value Value to set.
     * @return Builder instance.
     */
    public AliasBuilder withSetting(String key, boolean value) {
        settings.put(key, value);
        return this;
    }

    public CommandAlias build() {
        SimpleCommandAlias commandAlias = new SimpleCommandAlias(
                label, aliases, actions, aliasRunConditions, postprocessorHooks, settings
        );
        commandAlias.setMatchPriority(matchPriority);

        return commandAlias;
    }

}
