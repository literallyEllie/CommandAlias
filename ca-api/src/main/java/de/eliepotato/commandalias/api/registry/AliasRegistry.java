package de.eliepotato.commandalias.api.registry;

import de.eliepotato.commandalias.api.alias.CommandAlias;

import java.util.Collection;

/**
 * Registry for aliases.
 */
public interface AliasRegistry {

    /**
     * @return An unmodifiable collection of registered aliases.
     */
    Collection<CommandAlias> getRegisteredAliases();

    /**
     * Register aliases
     *
     * @param aliases Aliases to register.
     */
    void registerAlias(CommandAlias... aliases);

    /**
     * Unregister an alias by its reference.
     *
     * @param alias Alias reference to unregister.
     */
    void unregisterAlias(CommandAlias alias);

    /**
     * Unregister all aliases with a label.
     * </p>
     * i.e {@link CommandAlias#getLabel()}
     * This is a case-insensitive removal.
     *
     * @param label Label to unregister by.
     */
    void unregisterAlias(String label);

}
