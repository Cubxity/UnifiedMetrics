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

import dev.cubxity.plugins.metrics.api.metric.collector.MILLISECONDS_PER_SECOND
import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.api.metric.data.CounterSample
import dev.cubxity.plugins.metrics.api.metric.data.MetricSample
import java.lang.management.ManagementFactory

class GCCollector : MetricCollector {
    private val beans = ManagementFactory.getGarbageCollectorMXBeans()

    override fun collect(): List<MetricSample> = beans.map {
        CounterSample("jvm_gc_seconds_total", it.collectionTime / MILLISECONDS_PER_SECOND, mapOf("gc" to it.name))
    }
}