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
import dev.cubxity.plugins.metrics.api.metric.MetricsDriver
import dev.cubxity.plugins.metrics.prometheus.config.PrometheusConfig
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.HTTPServer
import java.net.InetSocketAddress

class PrometheusMetricsDriver(api: UnifiedMetrics, private val config: PrometheusConfig) : MetricsDriver {
    private val registry = CollectorRegistry()
    private var server: HTTPServer? = null

    init {
        registry.register(UnifiedMetricsCollector(api))
    }

    override fun initialize() {
        server = HTTPServer(InetSocketAddress(config.http.port), registry)
    }

    override fun close() {
        server?.stop()
        server = null
    }
}