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

import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.api.metric.data.CounterMetric
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import java.lang.management.ManagementFactory

class ThreadCollector : MetricCollector {
    private val bean = ManagementFactory.getThreadMXBean()

    override fun collect(): List<Metric> = listOf(
        GaugeMetric("jvm_threads_current_count", value = bean.threadCount),
        GaugeMetric("jvm_threads_daemon_count", value = bean.daemonThreadCount),
        CounterMetric("jvm_threads_started_total", value = bean.totalStartedThreadCount),
        GaugeMetric("jvm_threads_peak", value = bean.peakThreadCount),
    )
}