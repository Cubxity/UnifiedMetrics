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

package dev.cubxity.plugins.metrics.common.metric.system.memory

import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import java.lang.management.ManagementFactory

class MemoryCollector : MetricCollector {
    private val bean = ManagementFactory.getMemoryMXBean()

    override fun collect(): List<Metric> {
        val heapUsage = bean.heapMemoryUsage
        val nonHeapUsage = bean.nonHeapMemoryUsage

        val heapTags = mapOf("area" to "heap")
        val nonHeapTags = mapOf("area" to "nonheap")

        return listOf(
            GaugeMetric("jvm_memory_bytes_used", heapTags, heapUsage.used),
            GaugeMetric("jvm_memory_bytes_used", nonHeapTags, nonHeapUsage.used),
            GaugeMetric("jvm_memory_bytes_committed", heapTags, heapUsage.committed),
            GaugeMetric("jvm_memory_bytes_committed", nonHeapTags, nonHeapUsage.committed),
            GaugeMetric("jvm_memory_bytes_max", heapTags, heapUsage.max),
            GaugeMetric("jvm_memory_bytes_max", nonHeapTags, nonHeapUsage.max),
            GaugeMetric("jvm_memory_bytes_init", heapTags, heapUsage.init),
            GaugeMetric("jvm_memory_bytes_init", nonHeapTags, nonHeapUsage.init)
        )
    }
}