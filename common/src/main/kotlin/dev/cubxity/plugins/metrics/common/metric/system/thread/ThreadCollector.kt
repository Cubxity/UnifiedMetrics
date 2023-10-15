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

package dev.cubxity.plugins.metrics.common.metric.system.thread

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.NANOSECONDS_PER_SECOND
import dev.cubxity.plugins.metrics.api.metric.data.CounterMetric
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import java.lang.management.ManagementFactory

class ThreadCollector : Collector {
    private val bean = ManagementFactory.getThreadMXBean()

    override fun collect(): List<Metric> {
        val ids = bean.allThreadIds
        val list = ArrayList<Metric>(4 + 2 * ids.size)

        list += GaugeMetric("jvm_threads_current_count", value = bean.threadCount)
        list += GaugeMetric("jvm_threads_daemon_count", value = bean.daemonThreadCount)
        list += CounterMetric("jvm_threads_started_total", value = bean.totalStartedThreadCount)
        list += GaugeMetric("jvm_threads_peak", value = bean.peakThreadCount)

        ids.forEach { id ->
            val info = bean.getThreadInfo(id) ?: return@forEach
            val labels = mapOf("thread" to info.threadName)
            list += CounterMetric(
                "jvm_threads_cpu_time_total",
                value = bean.getThreadCpuTime(id) / NANOSECONDS_PER_SECOND,
                labels = labels
            )
            list += CounterMetric(
                "jvm_threads_user_time_total",
                value = bean.getThreadUserTime(id) / NANOSECONDS_PER_SECOND,
                labels = labels
            )
        }
        return list
    }
}