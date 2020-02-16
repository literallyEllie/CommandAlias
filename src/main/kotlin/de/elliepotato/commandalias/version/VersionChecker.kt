package de.elliepotato.commandalias.version

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.elliepotato.commandalias.CommandAlias
import org.bukkit.ChatColor
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.logging.Level

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
class VersionChecker(commandAlias: CommandAlias) {

    companion object {
        private val DOMAIN = "api.elliepotato.de"
        private val API_VERSION = "v1"

        private val ENDPOINT = "plugin"
        private val METHOD = "GetVersion"

        private val PLUGIN_ID = "command-alias"
    }

    private val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

    init {
        commandAlias.server.scheduler.runTaskAsynchronously(commandAlias, Runnable {
            Thread.currentThread().name = "command-alias-version-checker"

            val selfVersion = VersionParser(commandAlias.description.version)
            if (selfVersion.classifier == "DEV") {
                return@Runnable
            }

            val latestVersionRaw = getLatestVersion(commandAlias) ?: return@Runnable

            val latestVersion = VersionParser(latestVersionRaw)

            if (latestVersion.version > selfVersion.version || latestVersion.subVersion > selfVersion.subVersion) {
                commandAlias.updateMeMessage = ChatColor.BOLD.toString() + "Plugin outdated!" + ChatColor.RESET + " New version: " + ChatColor.RED +
                        latestVersion.fullVersion + ChatColor.WHITE + " (Your version: " + ChatColor.RED + selfVersion.fullVersion + ChatColor.WHITE + ")."
                commandAlias.log(ChatColor.stripColor(commandAlias.updateMeMessage)!!)
                return@Runnable
            }

            // If they have a snapshot build
            // And the latest version is BIGGER OR SAME
            // And SUB build is BIGGER

            if (latestVersion.classifier == "RELEASE" && selfVersion.classifier == "SNAPSHOT"
                    && selfVersion.version <= latestVersion.version
                    && selfVersion.subVersion <= latestVersion.subVersion) {

                commandAlias.updateMeMessage = ChatColor.BOLD.toString() + "Plugin outdated!" + ChatColor.RESET + " A stable version was released: " + ChatColor.RED +
                        latestVersion.fullVersion + ChatColor.WHITE + " (Your version: " + ChatColor.RED + selfVersion.fullVersion + ChatColor.WHITE + ")."
                commandAlias.log(ChatColor.stripColor(commandAlias.updateMeMessage)!!)
                return@Runnable
            }

        })

    }

    private fun getLatestVersion(commandAlias: CommandAlias): String? {
        try {
            val url = URL("https://$DOMAIN/$API_VERSION")

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"

            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)")
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            connection.doInput = true
            connection.doOutput = true
            connection.connectTimeout = 10000

            // Write
            val writer = OutputStreamWriter(connection.outputStream, StandardCharsets.UTF_8)
            writer.write(gson.toJson(PayloadWrapper()
                    .setEndpoint(ENDPOINT)
                    .setMethod(METHOD)
                    .addPayload("plugin-id", PLUGIN_ID)))

            writer.close()

            // Read
            val reader = BufferedReader(InputStreamReader(connection.inputStream))

            val rawOutput = reader.readLine()

            val response: PayloadWrapper = gson.fromJson<Any>(rawOutput, object : TypeToken<PayloadWrapper>() {
            }.type) as PayloadWrapper

            if (response.code != 200) {
                return null
            }

            reader.close()
            connection.disconnect()

            return response.message
        } catch (e: IOException) {
            commandAlias.log("Failed to check for an update! Ensure '$DOMAIN' is whitelisted! Maybe server is down?", Level.SEVERE)
            e.printStackTrace()
        }

        return null
    }


}