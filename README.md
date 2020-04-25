# CommandAlias
The source for an easy to setup, lightweight plugin for your Spigot server written in Kotlin. [Resource Page](https://www.spigotmc.org/resources/commandalias.44362/)

## Developer API
Since 1.5-RELEASE, there is real support for API usage. This API can be used for
- (Un)registering aliases, run conditions, placeholders
- Toggling an alias
- Processing a placeholder string

### Accessing the developer API
1. Add "CommandAlias" as a dependency or soft dependency to your plugin. 
2. Add the jar to the dependencies (sorry no maven repo)
3. EITHER listen on **CAPluginReloadEvent** (see the note below to see why)
4. OR Get the instance of the plugin through JavaPlugin#getPlugin or through PluginManager#getPlugin 
5. Cast said instance to the 
[CommandAliasAPI](https://github.com/literallyEllie/CommandAlias/tree/master/src/main/kotlin/de/elliepotato/commandalias/CommandAliasAPI.kt) interface.
6. Have fun

##### Important note
Register your aliases/placeholders/run conditions on the event 
[CAPluginReloadEvent](https://github.com/literallyEllie/CommandAlias/tree/master/src/main/kotlin/de/elliepotato/commandalias/event/CAPluginReloadEvent.kt).
Why? Because this event is called whenever the plugin is reloaded, including at startup.
When the plugin is reloaded it will clear everything. Though, to do this, you have to load before CommandAlias so you can register before the event is called. Or you load after and register in your onEnable and then again when CAPluginReloadEvent is called.

##### To actually add your add your own stuff:
1. Make an instance of what you want to make (RunCondition/AliasCommand/CAHook)
2. Register it with the API via the event. If you read the API it will become clear.

If you have any queries come to https://www.elliepotato.de/support
