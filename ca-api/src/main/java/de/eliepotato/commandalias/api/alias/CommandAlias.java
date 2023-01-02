package de.eliepotato.commandalias.api.alias;

import de.eliepotato.commandalias.api.executor.context.ExecutionContext;
import de.eliepotato.commandalias.api.hook.PostprocessorHook;
import de.eliepotato.commandalias.api.hook.AliasRunCondition;
import de.eliepotato.commandalias.api.util.AliasPriority;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CommandAlias extends Comparable<CommandAlias> {

    String getLabel();

    Set<String> getAliases();

    boolean isEnabled();

    void setEnabled(boolean enabled);

    AliasType getType();

    void setType(AliasType newType);

    AliasPriority getMatchPriority();

    void setMatchPriority(AliasPriority newPriority);

    List<AliasRunCondition> getPreprocessorHooks();

    List<PostprocessorHook> getPostprocessorHooks();

    boolean execute(ExecutionContext context);

    Map<String, Boolean> getSettings();

    default boolean getSetting(String key, boolean defaultValue) {
        return getSettings().getOrDefault(key, defaultValue);
    }

    @Override
    default int compareTo(@NotNull CommandAlias o) {
        return o.getMatchPriority().compareTo(getMatchPriority());
    }
}
