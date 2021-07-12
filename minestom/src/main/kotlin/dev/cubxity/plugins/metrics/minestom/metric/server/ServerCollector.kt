/*
 *     This file is part of UnifiedMetrics.
 *
 *     UnifiedMetrics is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     UnifiedMetrics is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with UnifiedMetrics.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cubxity.plugins.metrics.minestom.metric.server

import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import net.minestom.server.MinecraftServer

class ServerCollector : MetricCollector {
    override fun collect(): List<Metric> {
        val extensionCount = MinecraftServer.getExtensionManager().extensions.size
        val playerCount = MinecraftServer.getConnectionManager().onlinePlayers.size

        return listOf(
            GaugeMetric("minecraft_plugins", value = extensionCount),
            GaugeMetric("minecraft_players_count", value = playerCount),
            // Minestom does not have a "max players" count
            GaugeMetric("minecraft_players_max", value = playerCount + 1)
        )
    }
}