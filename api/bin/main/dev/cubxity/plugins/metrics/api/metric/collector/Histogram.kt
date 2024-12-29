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
import dev.cubxity.plugins.metrics.api.metric.data.Labels
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.api.metric.store.DoubleAdderStore
import dev.cubxity.plugins.metrics.api.metric.store.DoubleStoreFactory
import dev.cubxity.plugins.metrics.api.metric.store.LongAdderStore
import dev.cubxity.plugins.metrics.api.metric.store.LongStoreFactory

private val defaultBuckets = doubleArrayOf(
    .001, // 1 ms
    .005, // 5 ms
    .01, // 10 ms
    .02, // 20 ms
    .03, // 30 ms
    .04, // 40 ms
    .05, // 50 ms
    .075, // 75 ms
    .1, // 100 ms
    .25, // 250 ms
    .5, // 500 ms
    .75, // 750 ms
    1.0, // 1 s
    2.5, // 2.5 s
    5.0, // 5 s
    7.5, // 7.5 s
    10.0, // 10 s
    60.0 // 1 m
)

/**
 * @param name name of the sample.
 */
class Histogram(
    private val name: String,
    private val labels: Labels = emptyMap(),
    upperBounds: DoubleArray = defaultBuckets,
    sumStoreFactory: DoubleStoreFactory = DoubleAdderStore,
    countStoreFactory: LongStoreFactory = LongAdderStore
) : Collector {
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
            HistogramMetric(name, labels, count.toDouble(), sum.get(), bucket as Array<Bucket>)
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
