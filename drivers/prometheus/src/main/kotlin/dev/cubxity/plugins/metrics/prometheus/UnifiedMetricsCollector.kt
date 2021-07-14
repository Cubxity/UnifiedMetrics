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

package dev.cubxity.plugins.metrics.prometheus

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.api.metric.data.CounterMetric
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.HistogramMetric
import dev.cubxity.plugins.metrics.api.util.fastForEach
import dev.cubxity.plugins.metrics.api.util.toGoString
import io.prometheus.client.Collector
import kotlinx.coroutines.runBlocking

class UnifiedMetricsCollector(private val api: UnifiedMetrics) : Collector() {
    override fun collect(): List<MetricFamilySamples> {
        return try {
            val metrics = runBlocking {
                api.metricsManager.collect()
            }

            metrics.map { metric ->
                val keys = metric.labels.keys.toList()
                val values = metric.labels.values.toList()

                when (metric) {
                    is CounterMetric -> {
                        val samples = listOf(MetricFamilySamples.Sample(metric.name, keys, values, metric.value))
                        MetricFamilySamples(metric.name, Type.COUNTER, "", samples)
                    }
                    is GaugeMetric -> {
                        val samples = listOf(MetricFamilySamples.Sample(metric.name, keys, values, metric.value))
                        MetricFamilySamples(metric.name, Type.GAUGE, "", samples)
                    }
                    is HistogramMetric -> {
                        val samples = ArrayList<MetricFamilySamples.Sample>(2 + metric.bucket.size)

                        val keysWithLe = keys.toMutableList()
                        keysWithLe += "le"

                        val bucketName = "${metric.name}_bucket"

                        metric.bucket.fastForEach { bucket ->
                            val valuesWithLe = values.toMutableList()
                            valuesWithLe += bucket.upperBound.toGoString()

                            samples += MetricFamilySamples.Sample(
                                bucketName,
                                keysWithLe,
                                valuesWithLe,
                                bucket.cumulativeCount
                            )
                        }

                        samples.add(
                            MetricFamilySamples.Sample("${metric.name}_count", keys, values, metric.sampleCount)
                        )
                        samples.add(
                            MetricFamilySamples.Sample("${metric.name}_sum", keys, values, metric.sampleSum)
                        )

                        MetricFamilySamples(metric.name, Type.HISTOGRAM, "", samples)
                    }
                }
            }
        } catch (exception: Exception) {
            api.logger.severe("An error occurred whilst collecting metrics", exception)
            emptyList()
        }
    }
}