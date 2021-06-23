/*
 *     UnifiedMetrics is a fully-featured metrics collection plugin for Minecraft servers.
 *     Copyright (C) 2021  Cubxity
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cubxity.plugins.metrics.influx.config

import com.influxdb.client.InfluxDBClientOptions
import kotlinx.serialization.Serializable

@Serializable
data class InfluxConfig(
    val output: InfluxOutputConfig = InfluxOutputConfig(),
    val authentication: InfluxAuthenticationConfig = InfluxAuthenticationConfig()
)

@Serializable
data class InfluxOutputConfig(
    val url: String = "http://influxdb:8086",
    val organization: String = "-",
    val bucket: String = "unifiedmetrics",
    val interval: Long = 10
)

@Serializable
data class InfluxAuthenticationConfig(
    val scheme: InfluxDBClientOptions.AuthScheme = InfluxDBClientOptions.AuthScheme.TOKEN,
    val username: String = "influx",
    val password: String = "influx",
    val token: String = "insert_your_token"
)
