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

package dev.cubxity.plugins.metrics.prometheus.exporter

import dev.cubxity.plugins.metrics.api.metric.data.CounterMetric
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.HistogramMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.api.util.fastForEach
import dev.cubxity.plugins.metrics.api.util.toGoString
import io.prometheus.client.Collector
import java.io.Closeable

interface PrometheusExporter : Closeable {
    fun initialize()
}

fun List<Metric>.toPrometheus(): List<Collector.MetricFamilySamples> {
    val map = LinkedHashMap<String, MutableList<Metric>>(size)

    fastForEach { metric ->
        map.computeIfAbsent(metric.name) { ArrayList() }.add(metric)
    }

    return map.map { (name, metrics) ->
        val type: Collector.Type
        val samples: MutableList<Collector.MetricFamilySamples.Sample>

        when (val metric = metrics.first()) {
            is CounterMetric -> {
                type = Collector.Type.COUNTER
                samples = ArrayList(metrics.size)
            }
            is GaugeMetric -> {
                type = Collector.Type.GAUGE
                samples = ArrayList(metrics.size)
            }
            is HistogramMetric -> {
                type = Collector.Type.HISTOGRAM
                samples = ArrayList((2 + metric.bucket.size) * metrics.size)
            }
        }

        metrics.fastForEach { metric ->
            val keys = metric.labels.keys.toList()
            val values = metric.labels.values.toList()

            when (metric) {
                is CounterMetric -> {
                    samples += Collector.MetricFamilySamples.Sample(metric.name, keys, values, metric.value)
                }
                is GaugeMetric -> {
                    samples += Collector.MetricFamilySamples.Sample(metric.name, keys, values, metric.value)
                }
                is HistogramMetric -> {
                    val keysWithLe = keys.toMutableList()
                    keysWithLe += "le"

                    val bucketName = "${metric.name}_bucket"

                    metric.bucket.fastForEach { bucket ->
                        val valuesWithLe = values.toMutableList()
                        valuesWithLe += bucket.upperBound.toGoString()

                        samples += Collector.MetricFamilySamples.Sample(
                            bucketName,
                            keysWithLe,
                            valuesWithLe,
                            bucket.cumulativeCount
                        )
                    }

                    samples +=
                        Collector.MetricFamilySamples.Sample("${metric.name}_count", keys, values, metric.sampleCount)

                    samples +=
                        Collector.MetricFamilySamples.Sample("${metric.name}_sum", keys, values, metric.sampleSum)
                }
            }
        }

        Collector.MetricFamilySamples(name, type, "", samples)
    }
}