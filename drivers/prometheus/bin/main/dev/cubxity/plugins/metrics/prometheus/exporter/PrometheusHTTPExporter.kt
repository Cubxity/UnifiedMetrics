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

import com.sun.net.httpserver.BasicAuthenticator
import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.prometheus.PrometheusMetricsDriver
import dev.cubxity.plugins.metrics.prometheus.collector.UnifiedMetricsCollector
import dev.cubxity.plugins.metrics.prometheus.config.AuthenticationScheme
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.HTTPServer

class PrometheusHTTPExporter(
    private val api: UnifiedMetrics,
    private val driver: PrometheusMetricsDriver
) : PrometheusExporter {
    private var server: HTTPServer? = null

    override fun initialize() {
        val registry = CollectorRegistry()
        registry.register(UnifiedMetricsCollector(api))

        server = HTTPServer.Builder()
            .withHostname(driver.config.http.host)
            .withPort(driver.config.http.port)
            .withRegistry(registry)
            .apply {
                with(driver.config.http.authentication) {
                    if (scheme == AuthenticationScheme.Basic) {
                        withAuthenticator(Authenticator(username, password))
                    }
                }
            }
            .build()
    }

    override fun close() {
        server?.close()
        server = null
    }

    private class Authenticator(
        private val username: String,
        private val password: String
    ) : BasicAuthenticator("unifiedmetrics") {
        override fun checkCredentials(username: String?, password: String?): Boolean =
            this.username == username && this.password == password
    }
}