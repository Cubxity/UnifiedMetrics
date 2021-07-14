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
import dev.cubxity.plugins.metrics.api.metric.data.Labels
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.api.metric.store.DoubleAdderStore
import dev.cubxity.plugins.metrics.api.metric.store.DoubleStoreFactory

/**
 * @param name name of the sample. Should end with '_total'
 */
class Counter(
    private val name: String,
    private val labels: Labels = emptyMap(),
    valueStoreFactory: DoubleStoreFactory = DoubleAdderStore
) : Collector {
    private val count = valueStoreFactory.create()

    override fun collect(): List<Metric> {
        return listOf(
            CounterMetric(name, labels, count.get())
        )
    }

    operator fun inc(): Counter = apply {
        count.add(1.0)
    }

    operator fun plusAssign(delta: Double) {
        count.add(delta)
    }

    operator fun plusAssign(delta: Number) {
        count.add(delta.toDouble())
    }
}