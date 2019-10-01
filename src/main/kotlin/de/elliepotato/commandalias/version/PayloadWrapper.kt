package de.elliepotato.commandalias.version

import com.google.common.collect.Maps
import com.google.gson.annotations.Expose

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
class PayloadWrapper {

    lateinit var endpoint: String
    lateinit var method: String

    @Expose
    val payload: HashMap<String, String> = Maps.newHashMap()

    @Expose(serialize = false)
    var code = 0
    @Expose(serialize = false)
    lateinit var message: String

    fun addPayload(key: String, value: String): PayloadWrapper {
        this.payload[key] = value
        return this
    }

    fun setMethod(method: String): PayloadWrapper {
        this.method = method
        return this
    }

    fun setEndpoint(endpoint: String): PayloadWrapper {
        this.endpoint = endpoint
        return this
    }

}