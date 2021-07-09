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

package dev.cubxity.plugins.metrics.common.metric.system

import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.api.metric.data.GaugeSample
import dev.cubxity.plugins.metrics.api.metric.data.MetricSample
import java.lang.management.ManagementFactory

class MemoryCollector : MetricCollector {
    private val bean = ManagementFactory.getMemoryMXBean()

    override fun collect(): List<MetricSample> {
        val heapUsage = bean.heapMemoryUsage
        val nonHeapUsage = bean.nonHeapMemoryUsage

        val heapTags = mapOf("area" to "heap")
        val nonHeapTags = mapOf("area" to "nonheap")

        return listOf(
            GaugeSample("jvm_memory_bytes_used", heapUsage.used, heapTags),
            GaugeSample("jvm_memory_bytes_used", nonHeapUsage.used, nonHeapTags),
            GaugeSample("jvm_memory_bytes_committed", heapUsage.committed, heapTags),
            GaugeSample("jvm_memory_bytes_committed", nonHeapUsage.committed, nonHeapTags),
            GaugeSample("jvm_memory_bytes_max", heapUsage.max, heapTags),
            GaugeSample("jvm_memory_bytes_max", nonHeapUsage.max, nonHeapTags),
            GaugeSample("jvm_memory_bytes_init", heapUsage.init, heapTags),
            GaugeSample("jvm_memory_bytes_init", nonHeapUsage.init, nonHeapTags)
        )
    }
}