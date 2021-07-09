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
import dev.cubxity.plugins.metrics.api.metric.collect
import dev.cubxity.plugins.metrics.api.metric.data.MetricType
import io.prometheus.client.Collector
import kotlinx.coroutines.runBlocking

class UnifiedMetricsCollector(private val api: UnifiedMetrics) : Collector() {
    override fun collect(): List<MetricFamilySamples> = runBlocking(api.dispatcher) {
        try {
            api.metricsManager.collect().map {
                val keys = it.tags.keys.toList()
                val values = it.tags.values.toList()
                val sample = MetricFamilySamples.Sample(it.name, keys, values, it.value)
                val type = when (it.type) {
                    MetricType.Counter -> Type.COUNTER
                    MetricType.Gauge -> Type.GAUGE
                    else -> Type.UNKNOWN
                }

                MetricFamilySamples(it.name, type, "", listOf(sample))
            }
        } catch (exception: Exception) {
            api.logger.severe("An error occurred whilst collecting metrics", exception)
            emptyList()
        }
    }
}