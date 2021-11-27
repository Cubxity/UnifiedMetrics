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

package dev.cubxity.plugins.metrics.fabric.metrics.server.world

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.common.metric.Metrics
import dev.cubxity.plugins.metrics.fabric.bootstrap.UnifiedMetricsFabricBootstrap
import net.minecraft.util.WorldSavePath
import java.nio.file.Files

class ServerWorldFSCollector(private val bootstrap: UnifiedMetricsFabricBootstrap) : Collector {
    override fun collect(): List<Metric> {
        val worlds = bootstrap.server.worlds
        val samples = ArrayList<Metric>(worlds.count() * 3)

        val path = bootstrap.server.getSavePath(WorldSavePath.ROOT)
        val store = Files.getFileStore(path)
        val totalSpace = store.totalSpace
        val freeSpace = store.unallocatedSpace
        val usableSpace = store.usableSpace

        worlds.forEach { world ->
            val tags = mapOf("world" to world.registryKey.value.toString())
            samples.add(GaugeMetric(Metrics.Server.WorldFileSystemSize, tags, totalSpace))
            samples.add(GaugeMetric(Metrics.Server.WorldFileSystemFree, tags, freeSpace))
            samples.add(GaugeMetric(Metrics.Server.WorldFileSystemAvailable, tags, usableSpace))
        }

        return samples
    }
}