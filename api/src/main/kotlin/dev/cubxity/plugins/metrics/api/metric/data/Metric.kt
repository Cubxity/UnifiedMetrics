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

typealias Labels = Map<String, String>

sealed class Metric(
    val name: String,
    val labels: Labels = emptyMap()
)

class GaugeMetric(
    name: String,
    labels: Labels = emptyMap(),
    val value: Double
) : Metric(name, labels) {
    constructor(
        name: String,
        labels: Labels = emptyMap(),
        value: Number
    ) : this(name, labels, value.toDouble())
}

class CounterMetric(
    name: String,
    labels: Labels = emptyMap(),
    val value: Double
) : Metric(name, labels) {
    constructor(
        name: String,
        labels: Labels = emptyMap(),
        value: Number
    ) : this(name, labels, value.toDouble())
}

class HistogramMetric(
    name: String,
    labels: Labels = emptyMap(),
    val sampleCount: Double,
    val sampleSum: Double,
    val bucket: Array<Bucket>
) : Metric(name, labels) {
    constructor(
        name: String,
        labels: Labels = emptyMap(),
        sampleCount: Number,
        sampleSum: Number,
        bucket: Array<Bucket>
    ) : this(name, labels, sampleCount.toDouble(), sampleSum.toDouble(), bucket)
}

data class Bucket(val upperBound: Double, val cumulativeCount: Double)
