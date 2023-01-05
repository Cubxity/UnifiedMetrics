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

package dev.cubxity.plugins.metrics.forge.metrics.world

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.common.metric.Metrics
import dev.cubxity.plugins.metrics.forge.bootstrap.UnifiedMetricsForgeBootstrap

class WorldCollector(private val bootstrap: UnifiedMetricsForgeBootstrap) : Collector {
    override fun collect(): List<Metric> {
        val worlds = bootstrap.server.allLevels
        val samples = ArrayList<Metric>(worlds.count() * 3)

        worlds.forEach { world ->
            val tags = mapOf("world" to world.dimension().registry().toString())
            samples.add(GaugeMetric(Metrics.Server.WorldEntitiesCount, tags, world.allEntities.count()))
            samples.add(GaugeMetric(Metrics.Server.WorldPlayersCount, tags, world.players().size))
            samples.add(GaugeMetric(Metrics.Server.WorldLoadedChunks, tags, world.chunkSource.loadedChunksCount))
        }

        return samples
    }
}