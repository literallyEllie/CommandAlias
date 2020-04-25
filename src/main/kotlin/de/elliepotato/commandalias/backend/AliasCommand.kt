package de.elliepotato.commandalias.backend

import com.google.common.base.Joiner
import com.google.common.collect.Maps
import kotlin.math.max

/**
 * Created by Ellie on 16/09/2017 for CommandAlias.
 *
 *    Copyright 2017 Ellie
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
class AliasCommand(val label: String, var enabled: Boolean, val permission: String?, val aliases: List<String>,
                   val type: CommandType = CommandType.CMD, val runConditions: Map<String, Any> = Maps.newHashMap(),
                   val consoleCommand: String?) {

    private val placeholderIndexes: MutableList<Pair<Int, Int>>

    init {
        placeholderIndexes = parse()
    }

    /**
     * Inserts argument placeholders into the command label as configured by the user
     * and trails any on the end as long as no argument greater than the provided placeholder is requested.
     *
     * If there are no placeholders, it will just return the label.
     *
     */
    fun insertPlaceholders(args: List<String>): String {
        if (placeholderIndexes.isEmpty())
            return label

        val build = StringBuilder()

        // first = pos, second = arg needed
        var maxArg = -1;
        placeholderIndexes.forEachIndexed { index, data ->
            val part = label.substring(if (index == 0) 0 else placeholderIndexes[index - 1].first + 3, data.first)

            build.append(part)

            // if args are bigger than how many args need, cancel
            if (args.size - 1 < data.second) {
                return build.toString()
            }

            build.append(args[data.second])
            maxArg = max(maxArg, data.second)
        }


        // max arg will be +1 than args.size
        // do not want to append 0 arg as that is the command label.
        if (args.size > 1 && maxArg < args.size) {
            // append " " to avoid conjoining args + not breaking command if no args.
            if (build.isNotEmpty())
                build.append(" ")
            build.append(Joiner.on(" ").join(args.subList(max(maxArg + 1, 1), args.size)))
        }

        return build.toString()
    }

    fun serialiseLabel() = "${type.prefix}$label"

    /**
     * Parses the label and records positions of {..} indexed by the position where the first character of the
     * placeholder starts, i.e '{'
     * The value is the argument needed at the certain index.
     */
    private fun parse(): MutableList<Pair<Int, Int>> {
        var expectDigit = false
        var expectEndToken = false

        val startIndexes = mutableListOf<Pair<Int, Int>>()
        label.forEachIndexed { index, c ->
            // open
            if (c == '{') {
                expectDigit = true
                expectEndToken = false
                return@forEachIndexed
            }

            if (c.isDigit()) {
                if (expectDigit) {
                    expectDigit = false
                    expectEndToken = true
                } else {
                    expectEndToken = false
                }
                return@forEachIndexed
            } else if (expectDigit) {
                expectDigit = false
                return@forEachIndexed
            }

            if (c == '}') {
                if (expectEndToken) {
                    expectEndToken = false
                    startIndexes.add(Pair(index - 2, label[index - 1].toString().toInt()))
                } else
                    expectDigit = false
                return@forEachIndexed
            }

            expectDigit = false
            expectEndToken = false
        }

        return startIndexes;
    }

}