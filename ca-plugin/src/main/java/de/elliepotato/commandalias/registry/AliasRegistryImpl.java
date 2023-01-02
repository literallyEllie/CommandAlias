package de.elliepotato.commandalias.registry;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.registry.AliasRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AliasRegistryImpl implements AliasRegistry {

    private final List<CommandAlias> registered = new ArrayList<>();

    @Override
    public Collection<CommandAlias> getRegisteredAliases() {
        return registered;
    }

    @Override
    public void registerAlias(CommandAlias... aliases) {
        registered.addAll(Arrays.asList(aliases));
    }

    @Override
    public void unregisterAlias(CommandAlias alias) {
        registered.remove(alias);
    }

    @Override
    public void unregisterAlias(String label) {
        registered.removeIf(alias -> alias.getLabel().equalsIgnoreCase(label));
    }
}
