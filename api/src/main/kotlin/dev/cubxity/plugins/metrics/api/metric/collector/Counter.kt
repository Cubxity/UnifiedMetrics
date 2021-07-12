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

package dev.cubxity.plugins.metrics.api.metric.collector

import dev.cubxity.plugins.metrics.api.metric.data.CounterMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.api.metric.data.Tags
import dev.cubxity.plugins.metrics.api.metric.store.LongAdderStore
import dev.cubxity.plugins.metrics.api.metric.store.LongStoreFactory

/**
 * @param name name of the sample. Should end with '_total'
 */
class Counter(
    val name: String,
    val tags: Tags = emptyMap(),
    valueStoreFactory: LongStoreFactory = LongAdderStore
) : MetricCollector {
    private val count = valueStoreFactory.create()

    override fun collect(): List<Metric> {
        return listOf(
            CounterMetric(name, tags, count.get().toDouble())
        )
    }

    operator fun inc(): Counter = apply {
        count.add(1)
    }

    operator fun plusAssign(delta: Long) {
        count.add(delta)
    }

    operator fun plusAssign(delta: Number) {
        count.add(delta.toLong())
    }
}