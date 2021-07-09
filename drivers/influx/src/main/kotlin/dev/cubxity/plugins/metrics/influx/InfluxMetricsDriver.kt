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

package dev.cubxity.plugins.metrics.influx

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.InfluxDBClientFactory
import com.influxdb.client.InfluxDBClientOptions
import com.influxdb.client.WriteApi
import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.api.metric.MetricsDriver
import dev.cubxity.plugins.metrics.api.metric.collect
import dev.cubxity.plugins.metrics.api.metric.data.MetricSample
import dev.cubxity.plugins.metrics.influx.config.InfluxConfig
import kotlinx.coroutines.*
import com.influxdb.client.write.Point as InfluxPoint

class InfluxMetricsDriver(private val api: UnifiedMetrics, private val config: InfluxConfig) : MetricsDriver {
    private val coroutineScope = CoroutineScope(Dispatchers.Default) + SupervisorJob()

    private var influxDBClient: InfluxDBClient? = null
    private var writeApi: WriteApi? = null

    override fun initialize() {
        val options = InfluxDBClientOptions.builder()
            .url(config.output.url)
            .apply {
                when (config.authentication.scheme) {
                    InfluxDBClientOptions.AuthScheme.TOKEN ->
                        authenticateToken(config.authentication.token.toCharArray())
                    InfluxDBClientOptions.AuthScheme.SESSION ->
                        authenticate(config.authentication.username, config.authentication.password.toCharArray())
                }
            }
            .bucket(config.output.bucket)
            .org(config.output.organization)
            .build()

        influxDBClient = InfluxDBClientFactory.create(options)
        writeApi = influxDBClient?.writeApi

        scheduleTasks()
    }

    override fun close() {
        coroutineScope.cancel()
        writeApi?.close()
        influxDBClient?.close()
        influxDBClient = null
    }

    private fun scheduleTasks() {
        val interval = config.output.interval

        coroutineScope.launch {
            while (true) {
                try {
                    val samples = withContext(api.dispatcher) {
                        api.metricsManager.collect()
                    }
                    writeSamples(samples)
                } catch (error: Throwable) {
                    api.logger.severe("An error occurred whilst writing samples to InfluxDB", error)
                }
                delay(interval * 1000)
            }
        }
    }

    private fun writeSamples(samples: List<MetricSample>) {
        val writeApi = writeApi ?: return
        for (point in samples) {
            val influxPoint = InfluxPoint(point.name)
            influxPoint.addTags(point.tags)
            influxPoint.addTag("server", api.serverName)
            influxPoint.addField("value", point.value)

            writeApi.writePoint(influxPoint)
        }
    }
}