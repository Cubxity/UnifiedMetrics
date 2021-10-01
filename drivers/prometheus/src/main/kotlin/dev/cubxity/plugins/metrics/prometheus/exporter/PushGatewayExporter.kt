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
import dev.cubxity.plugins.metrics.prometheus.collector.MetricSamplesCollection
import dev.cubxity.plugins.metrics.prometheus.config.AuthenticationScheme
import io.prometheus.client.exporter.BasicAuthHttpConnectionFactory
import io.prometheus.client.exporter.PushGateway
import kotlinx.coroutines.*
import java.net.URL
import kotlin.math.max
import kotlin.system.measureTimeMillis

class PushGatewayExporter(
    private val api: UnifiedMetrics,
    private val driver: PrometheusMetricsDriver
) : PrometheusExporter {
    private val coroutineScope = CoroutineScope(Dispatchers.IO) + SupervisorJob()
    private val groupingKey = mapOf("server" to api.serverName)

    private var gateway: PushGateway? = null

    override fun initialize() {
        val config = driver.config.pushGateway

        gateway = PushGateway(URL(config.url)).apply {
            with(config.authentication) {
                if (scheme == AuthenticationScheme.Basic) {
                    setConnectionFactory(BasicAuthHttpConnectionFactory(username, password))
                }
            }
        }

        scheduleTasks()
    }

    override fun close() {
        coroutineScope.cancel()
        gateway = null
    }

    private fun scheduleTasks() {
        val interval = driver.config.pushGateway.interval * 1000

        coroutineScope.launch {
            while (true) {
                val time = measureTimeMillis {
                    try {
                        val samples = api.metricsManager.collect()
                        val collection = MetricSamplesCollection(samples.toPrometheus())

                        gateway?.push(collection, driver.config.pushGateway.job, groupingKey)
                    } catch (error: Throwable) {
                        api.logger.severe("An error occurred whilst writing samples to Prometheus", error)
                    }
                }
                delay(max(0, interval - time))
            }
        }
    }
}