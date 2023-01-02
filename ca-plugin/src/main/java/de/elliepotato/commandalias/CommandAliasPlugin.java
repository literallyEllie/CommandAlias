package de.elliepotato.commandalias;

import de.eliepotato.commandalias.api.executor.AliasExecutor;
import de.eliepotato.commandalias.api.registry.AliasRegistry;
import de.elliepotato.commandalias.executor.AliasExecutorImpl;
import de.elliepotato.commandalias.listener.CommandListener;
import de.elliepotato.commandalias.registry.AliasRegistryImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAliasPlugin extends JavaPlugin {

    private AliasRegistry registry;
    private AliasExecutor executor;

    @Override
    public void onEnable() {
        // ok so what is the plan..
        // run x: when [y..] is executed.
        //
        // have everything run on a listener + register to the command map?

        registry = new AliasRegistryImpl();
        executor = new AliasExecutorImpl();

        registerListeners();
    }

    @Override
    public void onDisable() {

    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new CommandListener(this), this);
    }

    public AliasRegistry getRegistry() {
        return registry;
    }

    public AliasExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(AliasExecutor executor) {
        this.executor = executor;
    }

}
