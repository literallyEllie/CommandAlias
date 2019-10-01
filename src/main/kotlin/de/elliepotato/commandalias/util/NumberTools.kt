package de.elliepotato.commandalias.util

import java.util.*
import kotlin.math.floor

/**
 * Created by Ellie on 01/10/2019 for CommandAlias.
 *
 *    Copyright 2019 Ellie
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
class NumberTools {

    companion object {
        fun tryParse(value: String): Optional<Int> {

            try {
                return Optional.of(Integer.parseInt(value))
            } catch (ignored: NumberFormatException) {
            }

            return Optional.empty()
        }

        fun equals(a: Int, b: Int): Boolean {
            return a == b
        }

        fun halfFloor(x: Int): Double {
            return floor((x / 2).toDouble())
        }

    }


}
