package de.elliepotato.commandalias.backend

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.*
import java.util.function.Consumer

/**
 * Created by Ellie on 23.7.17 for PublicPlugins.
 * Affiliated with www.elliepotato.de
 *
 */
class AliasConfig(dir:File) {

    val file: File = File(dir, "config.yml")
    val cfg: YamlConfiguration

    init {

        var first: Boolean = false

        if(!file.exists()){
            if(!file.createNewFile()) throw IOException("Failed to create config.yml!")
            first = true
        }

        cfg = YamlConfiguration.loadConfiguration(file)

        if(first){
            cfg.set("commands.gamemode.aliases", Arrays.asList("gm", "gamemodepls"))
            cfg.set("commands.gamemode.enabled", true)
            cfg.set("commands.online.aliases", Collections.singletonList("list"))
            cfg.set("commands.online.enabled", false)
            save(cfg)
        }
    }

    /**
     * @return A HashMap: key = alias, value = actual command
     */
    fun getCommands() : HashMap<String, String> {
        val cmds: HashMap<String, String> = HashMap()
        cfg.getConfigurationSection("commands").getKeys(false).forEach(Consumer { t ->
            val aliases: List<String> = cfg.getStringList("commands.$t.aliases")
            val enabled: Boolean = cfg.getBoolean("commands.$t.enabled")

            if(enabled) {
                for (alias in aliases) {
                    cmds.put(alias.toLowerCase(), t)
                }
            }
        })
        return cmds
    }

    fun save(config: YamlConfiguration){
        config.save(file)
    }


}