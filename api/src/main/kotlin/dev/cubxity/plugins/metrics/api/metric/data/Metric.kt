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

@file:Suppress("FunctionName")

package dev.cubxity.plugins.metrics.api.metric.data

typealias Tags = Map<String, String>

sealed class Metric(
    val name: String,
    val tags: Tags = emptyMap()
)

class GaugeMetric(
    name: String,
    tags: Tags = emptyMap(),
    val value: Double
) : Metric(name, tags) {
    constructor(
        name: String,
        tags: Tags = emptyMap(),
        value: Number
    ) : this(name, tags, value.toDouble())
}

class CounterMetric(
    name: String,
    tags: Tags = emptyMap(),
    val value: Double
) : Metric(name, tags) {
    constructor(
        name: String,
        tags: Tags = emptyMap(),
        value: Number
    ) : this(name, tags, value.toDouble())
}

class HistogramMetric(
    name: String,
    tags: Tags = emptyMap(),
    val sampleCount: Double,
    val sampleSum: Double,
    val bucket: Array<Bucket>
) : Metric(name, tags) {
    constructor(
        name: String,
        tags: Tags = emptyMap(),
        sampleCount: Number,
        sampleSum: Number,
        bucket: Array<Bucket>
    ) : this(name, tags, sampleCount.toDouble(), sampleSum.toDouble(), bucket)
}

data class Bucket(val upperBound: Double, val cumulativeCount: Double)
