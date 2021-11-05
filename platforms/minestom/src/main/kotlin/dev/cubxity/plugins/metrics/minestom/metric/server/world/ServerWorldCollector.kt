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

package dev.cubxity.plugins.metrics.minestom.metric.server.world

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.common.metric.Metrics
import net.minestom.server.MinecraftServer

class ServerWorldCollector : Collector {
    override fun collect(): List<Metric> {
        val instances = MinecraftServer.getInstanceManager().instances
        val samples = ArrayList<Metric>(instances.size * 3)

        for (instance in instances) {
            val tags = mapOf("world" to instance.uniqueId.toString())
            samples.add(GaugeMetric(Metrics.Server.WorldEntitiesCount, tags, instance.entities.size))
            samples.add(GaugeMetric(Metrics.Server.WorldPlayersCount, tags, instance.players.size))
            samples.add(GaugeMetric(Metrics.Server.WorldLoadedChunks, tags, instance.chunks.size))
        }

        return samples
    }
}