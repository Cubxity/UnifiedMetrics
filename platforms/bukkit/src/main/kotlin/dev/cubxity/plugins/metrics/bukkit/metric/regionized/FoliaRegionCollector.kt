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
import dev.cubxity.plugins.metrics.common.metric.Metrics
import org.bukkit.Bukkit
import org.bukkit.World
import java.util.function.Consumer

class FoliaRegionCollector : Collector {
    override fun collect(): List<Metric> {
        val worlds = Bukkit.getWorlds()
        val regions = ArrayList<Pair<String, Any>>()

        for (world in worlds) {
            computeForAllRegions(regioniser(world)) { region ->
                regions.add(world.name to region)
            }
        }

        val samples = ArrayList<Metric>(regions.size * 4 + 1)
        for ((worldName, region) in regions) {
            val data = invoke(region, "getData") ?: continue
            val tags = mapOf("world" to worldName, "region" to "${field(region, "id")}")
            samples.add(CounterMetric(Metrics.RegionizedServer.RegionTick, tags, currentTick(data)))

            val stats = invoke(data, "getRegionStats") ?: field(data, "regionStats")
            samples.add(GaugeMetric(Metrics.RegionizedServer.RegionEntitiesCount, tags, number(stats, "getEntityCount", "entityCount")))
            samples.add(GaugeMetric(Metrics.RegionizedServer.RegionPlayersCount, tags, number(stats, "getPlayerCount", "playerCount")))
            samples.add(GaugeMetric(Metrics.RegionizedServer.RegionChunksCount, tags, number(stats, "getChunkCount", "chunkCount")))
        }

        samples.add(GaugeMetric(Metrics.RegionizedServer.RegionCount, value = regions.size))
        return samples
    }

    private fun regioniser(world: World): Any {
        val handle = world.javaClass.getMethod("getHandle").invoke(world)
        return handle.javaClass.getField("regioniser").get(handle)
    }

    private fun computeForAllRegions(regioniser: Any, consumer: (Any) -> Unit) {
        regioniser.javaClass
            .getMethod("computeForAllRegions", Consumer::class.java)
            .invoke(regioniser, Consumer<Any> { consumer(it) })
    }

    private fun currentTick(data: Any): Number = number(data, "getCurrentTick", "currentTick")

    private fun number(target: Any, methodName: String, fieldName: String): Number {
        invoke(target, methodName)?.let { return it as Number }
        return field(target, fieldName) as Number
    }

    private fun invoke(target: Any, name: String): Any? = try {
        target.javaClass.getMethod(name).invoke(target)
    } catch (_: NoSuchMethodException) {
        null
    }

    private fun field(target: Any, name: String): Any =
        target.javaClass.getField(name).get(target)
}
