/*
 *     UnifiedMetrics is a fully-featured metrics collection plugin for Spigot.
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

package dev.cubxity.plugins.metrics

import org.bukkit.configuration.Configuration

class UnifiedMetricsConfig(config: Configuration) {
    val influx: Influx = Influx(
        config.getBoolean("influx.enabled", false),
        config.getString("influx.url", "http://influxdb:8086"),
        config.getString("influx.server", "main"),
        config.getString("influx.bucket", "unifiedmetrics"),
        config.getString("influx.username", "influx"),
        config.getString("influx.password", "influx"),
        config.getInt("influx.interval", 10)
    )

    val elastic: Elastic = Elastic(
        config.getBoolean("elastic.enabled", false),
        config.getString("elastic.url", "elasticsearch:9200")
    )

    data class Influx(
        val isEnabled: Boolean,
        val url: String,
        val server: String,
        val bucket: String,
        val username: String,
        val password: String,
        val interval: Int
    )

    data class Elastic(
        val isEnabled: Boolean,
        val url: String
    )
}