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

package dev.cubxity.plugins.metrics.bukkit.metric.regionized

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.data.CounterMetric
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.bukkit.util.regioniser
import dev.cubxity.plugins.metrics.common.metric.Metrics
import io.papermc.paper.threadedregions.ThreadedRegionizer.ThreadedRegion
import io.papermc.paper.threadedregions.TickRegions.TickRegionData
import io.papermc.paper.threadedregions.TickRegions.TickRegionSectionData
import org.bukkit.Bukkit


class FoliaServerCollector : Collector {
    override fun collect(): List<Metric> {
        val worlds = Bukkit.getWorlds()

        val regions = ArrayList<ThreadedRegion<TickRegionData, TickRegionSectionData>>()
        for (world in worlds) {
            world.regioniser.computeForAllRegions(regions::add)
        }

        val samples = ArrayList<Metric>(regions.size * 4 + 1)
        for (region in regions) {
            val tags = mapOf("world" to region.data.world.serverLevelData.levelName, "region" to "${region.id}")
            samples.add(CounterMetric(Metrics.RegionizedServer.RegionTick, tags, region.data.currentTick))

            val stats = region.data.regionStats
            samples.add(GaugeMetric(Metrics.RegionizedServer.RegionEntitiesCount, tags, stats.entityCount))
            samples.add(GaugeMetric(Metrics.RegionizedServer.RegionPlayersCount, tags, stats.playerCount))
            samples.add(GaugeMetric(Metrics.RegionizedServer.RegionChunksCount, tags, stats.chunkCount))
        }

        samples.add(GaugeMetric(Metrics.RegionizedServer.RegionCount, value = regions.size))

        return samples
    }
}