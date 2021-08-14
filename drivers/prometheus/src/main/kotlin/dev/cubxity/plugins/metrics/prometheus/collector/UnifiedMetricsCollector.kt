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

package dev.cubxity.plugins.metrics.prometheus.collector

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.prometheus.exporter.toPrometheus
import io.prometheus.client.Collector
import kotlinx.coroutines.runBlocking

class UnifiedMetricsCollector(private val api: UnifiedMetrics) : Collector() {
    override fun collect(): List<MetricFamilySamples> {
        return try {
            val metrics = runBlocking {
                api.metricsManager.collect()
            }

            metrics.toPrometheus()
        } catch (exception: Exception) {
            api.logger.severe("An error occurred whilst collecting metrics", exception)
            emptyList()
        }
    }
}