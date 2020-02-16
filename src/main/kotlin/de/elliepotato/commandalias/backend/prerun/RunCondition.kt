package de.elliepotato.commandalias.backend.prerun

import de.elliepotato.commandalias.backend.AliasCommand
import org.bukkit.entity.Player

/**
 * Created by Ellie on 15/02/2020 for CommandAlias.
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
interface RunCondition {

    /**
     * ID of the run condition as referenced in the config.
     */
    fun getId(): String

    /**
     * When this method is called, you can assume that the alias has this certain run condition
     * To access what the config value is see the value of AliasCommand#runConditions[getId]
     *
     * @param alias an instance of the alias run
     * @param args The raw args they provided (includes everything)
     * @param player the player who ran the command
     * @return whether the alias should run
     */
    fun meetsConditions(alias: AliasCommand, args: List<String>, player: Player): Boolean

}