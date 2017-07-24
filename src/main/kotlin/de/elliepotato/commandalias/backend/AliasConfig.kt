package de.elliepotato.commandalias.backend

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap

/**
 * Created by Ellie on 23.7.17 for PublicPlugins.
 * Affiliated with www.elliepotato.de
 *
 */
class AliasConfig(dir:File) {

    val file: File = File(dir, "config.yml")
    var cfg: YamlConfiguration

    init {

        var first: Boolean = false

        if (!file.exists()) {
            if (!file.createNewFile()) throw IOException("Failed to create config.yml!")
            first = true
        }

        cfg = YamlConfiguration.loadConfiguration(file)

        if (first) {
            cfg.set("prefix", "&7[&aCommandAlias&7] &c")
            cfg.set("commands.gamemode.aliases", Arrays.asList("gm", "gamemodepls"))
            cfg.set("commands.gamemode.enabled", true)
            cfg.set("commands.gamemode.permission", "")
            cfg.set("commands.list.aliases", Collections.singletonList("peepsonline"))
            cfg.set("commands.list.enabled", "my.custom.permission")
            save(cfg)
        }

        if(cfg.get("prefix") == null){
            cfg.set("prefix", "&7[&aCommandAlias&7] &c")
            save(cfg)
        }

    }

    /**
     * @return A HashMap: key = alias, value = actual command
     */
    fun getCommands(): HashMap<String, String> {
        val cmds: HashMap<String, String> = HashMap()
        cfg.getConfigurationSection("commands").getKeys(false).forEach(Consumer { t ->
            val aliases: List<String> = cfg.getStringList("commands.$t.aliases")
            val enabled: Boolean = cfg.getBoolean("commands.$t.enabled")

            if (enabled) {
                for (alias in aliases) {
                    cmds.put(alias.toLowerCase(), t)
                }
            }
        })
        return cmds
    }

    fun getPermissions(): HashMap<String, String> {
        val perms: HashMap<String, String> = HashMap()
        var changed:Boolean = false
        cfg.getConfigurationSection("commands").getKeys(false).forEach(Consumer { t ->
            if(cfg.get("commands.$t.permission") == null){
                cfg.set("commands.$t.permission", "") // support for v1.0
                changed = true
            }
            val perm: String = cfg.getString("commands.$t.permission")
            val enabled: Boolean = cfg.getBoolean("commands.$t.enabled")



            if (enabled) {
                perms.put(t, perm) // index by actual command
            }
        })
        if(changed) save(cfg)
        return perms
    }

    fun getPrefix(): String {
        return cfg.getString("prefix")
    }

    fun save(config: YamlConfiguration) {
        config.save(file)
    }

    fun reload() {
        cfg = YamlConfiguration.loadConfiguration(file)
    }

    fun toggleAlias(commands: HashMap<String, String>, label: String) : HashMap<String, String> {
        if(cfg.get("commands.${label.toLowerCase()}") == null){
            return commands
        }

        val enabled = cfg.getBoolean("commands.${label.toLowerCase()}.enabled")

        if(enabled) {
            // Remove it from current map
            val it = commands.iterator()
            for (mutableEntry in it) {
                if (mutableEntry.value == label.toLowerCase()) {
                    it.remove()
                }
            }
        }else{
            for (key in cfg.getStringList("commands.${label.toLowerCase()}.aliases")) {
                commands.put(key.toLowerCase(), label.toLowerCase())
            }
        }

        cfg.set("commands.${label.toLowerCase()}.enabled", !enabled)
        save(cfg)

        return commands
    }


}