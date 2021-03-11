/*
 *     UnifiedMetrics is a fully-featured metrics collection plugin for Minecraft servers.
 *     Copyright (C) 2021  Cubxity
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cubxity.plugins.metrics.prometheus

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.api.metric.collect
import dev.cubxity.plugins.metrics.api.metric.data.MetricType
import io.prometheus.client.Collector

class UnifiedMetricsCollector(private val api: UnifiedMetrics) : Collector() {
    override fun collect(): List<MetricFamilySamples> {
        return api.metricsManager.collect().map {
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
    }
}