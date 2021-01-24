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

package dev.cubxity.plugins.metrics.common.config

import com.uchuhimo.konf.ConfigSpec

object MetricsSpec : ConfigSpec("metrics") {
    val enabled by optional(true, "enabled")
    val interval by optional(10L, "interval", "Data collection interval, in seconds")

    object InfluxSpec : ConfigSpec("influx") {
        val url by optional("http://influxdb:8086", "url")
        val bucket by optional("unifiedmetrics", "bucket")
        val username by optional("influx", "influx")
        val password by optional("influx", "password")
    }
}