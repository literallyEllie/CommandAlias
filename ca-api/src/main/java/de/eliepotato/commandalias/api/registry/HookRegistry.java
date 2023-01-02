package de.eliepotato.commandalias.api.registry;

import de.eliepotato.commandalias.api.hook.AliasRunCondition;
import de.eliepotato.commandalias.api.hook.ExecutionHook;

import java.util.List;

public interface HookRegistry {

    List<ExecutionHook> getRegisteredHooks();

    List<AliasRunCondition> getRegisteredRunConditions();

    void registerHook(String id, ExecutionHook hook);


}
