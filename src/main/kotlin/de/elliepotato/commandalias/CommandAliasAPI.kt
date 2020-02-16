package de.elliepotato.commandalias

import de.elliepotato.commandalias.backend.AliasCommand
import de.elliepotato.commandalias.backend.prerun.RunCondition
import de.elliepotato.commandalias.hook.CAHook
import org.bukkit.entity.Player

/**
 * Created by Ellie on 16/02/2020 for CommandAlias.
 *
 *    Copyright 2020 Ellie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
interface CommandAliasAPI {

    /**
     * Registers an alias.
     * Should register on CAPluginReloadEvent
     */
    fun registerAlias(aliasCommand: AliasCommand)

    /**
     * Unregister a command alias by its primary label.
     */
    fun unregisterAlias(label: String)

    /**
     * Toggle an alias by its label.
     * This will update the config.
     *
     * @return if it was changed or not.
     *      If the alias did not exist, it will return false
     */
    fun toggleAlias(label: String): Boolean

    /**
     * Registers a run condition
     * Should register on CAPluginReloadEvent
     */
    fun registerRunCondition(runCondition: RunCondition)

    /**
     * Unregister a run condition by its id
     */
    fun unregisterRunCondition(id: String)

    /**
     * Registers a placeholder hook that will be processed when #processString
     */
    fun registerPlaceholderHook(hook: CAHook)

    /**
     * Unregister a placeholder hook
     */
    fun unregisterPlaceholderHook(hook: CAHook)

    /**
     * Processes the placeholders of a string
     *
     * @return the processed string
     */
    fun processString(message: String, sender: Player): String

}