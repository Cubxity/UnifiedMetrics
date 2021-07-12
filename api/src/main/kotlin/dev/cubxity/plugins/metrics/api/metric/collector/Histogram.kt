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

import dev.cubxity.plugins.metrics.api.metric.data.Bucket
import dev.cubxity.plugins.metrics.api.metric.data.HistogramMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.api.metric.data.Tags
import dev.cubxity.plugins.metrics.api.metric.store.DoubleAdderStore
import dev.cubxity.plugins.metrics.api.metric.store.DoubleStoreFactory
import dev.cubxity.plugins.metrics.api.metric.store.LongAdderStore
import dev.cubxity.plugins.metrics.api.metric.store.LongStoreFactory

private val defaultBuckets = doubleArrayOf(.001, .005, .01, .025, .05, .075, .1, .25, .5, .75, 1.0, 2.5, 5.0, 7.5, 10.0)

/**
 * @param name name of the sample.
 */
class Histogram(
    val name: String,
    val tags: Tags = emptyMap(),
    upperBounds: DoubleArray = defaultBuckets,
    sumStoreFactory: DoubleStoreFactory = DoubleAdderStore,
    countStoreFactory: LongStoreFactory = LongAdderStore
) : MetricCollector {
    private val upperBounds = upperBounds + Double.POSITIVE_INFINITY

    private val sum = sumStoreFactory.create()
    private val counts = Array(this.upperBounds.size) {
        countStoreFactory.create()
    }

    override fun collect(): List<Metric> {
        val bucketSize = counts.size
        val bucket = arrayOfNulls<Bucket>(bucketSize)
        var count = 0L

        for (i in bucketSize downTo 1) {
            val index = bucketSize - i

            count += counts[index].get()
            bucket[index] = Bucket(upperBounds[index], count.toDouble())
        }

        @Suppress("UNCHECKED_CAST")
        return listOf(
            HistogramMetric(name, tags, count.toDouble(), sum.get(), bucket as Array<Bucket>)
        )
    }

    operator fun plusAssign(value: Double) {
        val size = upperBounds.size
        for (i in size downTo 1) {
            val index = size - i
            if (value <= upperBounds[index]) {
                counts[index].add(1)
                break
            }
        }
        sum.add(value)
    }

    operator fun plusAssign(value: Number) {
        plusAssign(value.toDouble())
    }
}
