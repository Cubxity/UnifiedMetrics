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

import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.api.metric.data.Tags
import java.util.concurrent.atomic.AtomicLong

/**
 * @param name name of the sample.
 */
class Gauge(
    val name: String,
    val tags: Tags = emptyMap(),
) : MetricCollector {
    private val count = AtomicLong()

    override fun collect(): List<Metric> {
        return listOf(
            GaugeMetric(name, tags, count.get().toDouble())
        )
    }

    fun set(value: Long) {
        count.set(value)
    }

    operator fun inc() = apply {
        count.incrementAndGet()
    }

    operator fun dec() = apply {
        count.decrementAndGet()
    }

    operator fun plusAssign(delta: Long) {
        count.addAndGet(delta)
    }

    operator fun plusAssign(delta: Number) {
        count.addAndGet(delta.toLong())
    }

    operator fun minusAssign(delta: Long) {
        count.addAndGet(-delta)
    }

    operator fun minusAssign(delta: Number) {
        count.addAndGet(-delta.toLong())
    }
}