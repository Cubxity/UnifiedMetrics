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

package dev.cubxity.plugins.metrics.bukkit.metric.world

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.api.util.fastForEach
import dev.cubxity.plugins.metrics.bukkit.bootstrap.UnifiedMetricsBukkitBootstrap
import dev.cubxity.plugins.metrics.common.metric.Metrics

class BukkitWorldCollector(private val bootstrap: UnifiedMetricsBukkitBootstrap) : Collector {
    override fun collect(): List<Metric> {
        val worlds = bootstrap.server.worlds
        val samples = ArrayList<Metric>(worlds.size * 3)

        worlds.fastForEach { world ->
            val tags = mapOf("world" to world.name)
            samples.add(GaugeMetric(Metrics.Server.WorldEntitiesCount, tags, world.entities.size))
            samples.add(GaugeMetric(Metrics.Server.WorldPlayersCount, tags, world.players.size))
            samples.add(GaugeMetric(Metrics.Server.WorldLoadedChunks, tags, world.loadedChunks.size))
        }

        return samples
    }
}
