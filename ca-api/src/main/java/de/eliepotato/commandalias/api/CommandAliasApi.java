package de.eliepotato.commandalias.api;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.AliasExecutor;

/**
 * Main endpoint for the command alias api.
 */
public interface CommandAliasApi {

    /**
     * @return The alias executor.
     */
    AliasExecutor getExecutor();

    /**
     * Set a custom alias executor.
     * </p>
     * CONFIG VALUE must be set to true for custom
     * executors to be utilized.
     *
     * @param executor Executor to register.
     */
    void setExecutor(AliasExecutor executor);

    void registerAlias(CommandAlias alias);

    void unregisterAlias(CommandAlias alias);

    void unregisterAlias(String label);

}
