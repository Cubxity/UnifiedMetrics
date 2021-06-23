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

package dev.cubxity.plugins.metrics.bungee.metric.server

import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.api.metric.data.GaugeSample
import dev.cubxity.plugins.metrics.api.metric.data.MetricSample
import dev.cubxity.plugins.metrics.bungee.bootstrap.UnifiedMetricsBungeeBootstrap

class ServerCollector(private val bootstrap: UnifiedMetricsBungeeBootstrap) : MetricCollector {
    override fun collect(): List<MetricSample> {
        val proxy = bootstrap.proxy
        return listOf(
            GaugeSample("minecraft_plugins", proxy.pluginManager.plugins.size),
            GaugeSample("minecraft_players_count", proxy.onlineCount),
            GaugeSample("minecraft_players_max", proxy.config.playerLimit)
        )
    }
}