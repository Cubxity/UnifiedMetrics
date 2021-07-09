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

import dev.cubxity.plugins.metrics.api.metric.data.CounterSample
import dev.cubxity.plugins.metrics.api.metric.data.MetricSample
import java.util.concurrent.atomic.AtomicInteger

/**
 * @param name name of the sample. Should end with '_total'
 */
class Counter(val name: String) : MetricCollector {
    private val tags: MutableMap<String, String> = HashMap()
    private val count = AtomicInteger()

    override fun collect(): List<MetricSample> {
        val sample = CounterSample(name, count.get().toDouble(), tags)
        return listOf(sample)
    }

    fun inc() {
        count.incrementAndGet()
    }

    fun dec() {
        count.decrementAndGet()
    }

    operator fun plusAssign(delta: Int) {
        count.addAndGet(delta)
    }

    operator fun minusAssign(delta: Int) {
        count.addAndGet(-delta)
    }
}