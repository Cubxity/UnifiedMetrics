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

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.prometheus.PrometheusMetricsDriver
import dev.cubxity.plugins.metrics.prometheus.collector.UnifiedMetricsCollector
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.HTTPServer
import java.net.InetSocketAddress

class PrometheusHTTPExporter(private val api: UnifiedMetrics, private val driver: PrometheusMetricsDriver) : PrometheusExporter {
    private var server: HTTPServer? = null

    override fun initialize() {
        val registry = CollectorRegistry()
        registry.register(UnifiedMetricsCollector(api))

        server = HTTPServer(InetSocketAddress(driver.config.http.host, driver.config.http.port), registry)
    }

    override fun close() {
        server?.stop()
        server = null
    }
}