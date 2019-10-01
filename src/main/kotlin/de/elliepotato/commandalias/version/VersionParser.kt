package de.elliepotato.sleepy.version

import de.elliepotato.commandalias.util.NumberTools
import org.apache.commons.lang.StringUtils

import java.util.regex.Pattern

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
class VersionParser @Throws(IllegalArgumentException::class)
constructor(val fullVersion: String) {

    companion object {
        private val VERSION_TAG = Pattern.compile("^(\\d){1,2}[.](\\d){1,2}[-]([a-zA-Z]+)([-](\\d)+)?$")
    }

    val version: Int
    val subVersion: Int
    val classifier: String
    val build: Int

    init {

        val matcher = VERSION_TAG.matcher(fullVersion.trim { it <= ' ' })

        if (!matcher.find() && !matcher.matches()) {
            throw IllegalStateException("version string invalid")
        }

        val version = NumberTools.tryParse(matcher.group(1))
        this.version = version.orElseThrow { IllegalArgumentException("bad version string (" + matcher.group(1) + ")") }

        val subVersion = NumberTools.tryParse(matcher.group(2))
        this.subVersion = subVersion.orElseThrow { IllegalArgumentException("bad sub version string (" + matcher.group(2) + ")") }

        val buildClassifier = matcher.group(3)
        if (StringUtils.isEmpty(buildClassifier))
            throw IllegalArgumentException("bad version classifier (" + matcher.group(3) + ")")
        this.classifier = buildClassifier

        // dev
        val build = NumberTools.tryParse(matcher.group(5))
        this.build = build.orElse(0)
    }

}
