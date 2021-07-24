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
import com.influxdb.client.write.Point
import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.api.metric.MetricsDriver
import dev.cubxity.plugins.metrics.api.metric.data.CounterMetric
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.HistogramMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.api.util.fastForEach
import dev.cubxity.plugins.metrics.api.util.toGoString
import dev.cubxity.plugins.metrics.influx.config.InfluxConfig
import kotlinx.coroutines.*
import kotlin.math.max
import kotlin.system.measureTimeMillis

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
        val interval = config.output.interval * 1000

        coroutineScope.launch {
            while (true) {
                val time = measureTimeMillis {
                    try {
                        val metrics = api.metricsManager.collect()
                        writeMetrics(metrics)
                    } catch (error: Throwable) {
                        api.logger.severe("An error occurred whilst writing samples to InfluxDB", error)
                    }
                }
                delay(max(0, interval - time))
            }
        }
    }

    private fun writeMetrics(metrics: List<Metric>) {
        val writeApi = writeApi ?: return
        metrics.fastForEach { metric ->
            val point = Point(metric.name)
            point.addTags(metric.labels)
            point.addTag("server", api.serverName)

            when (metric) {
                is GaugeMetric -> point.addField("gauge", metric.value)
                is CounterMetric -> point.addField("counter", metric.value)
                is HistogramMetric -> {
                    metric.bucket.fastForEach { bucket ->
                        point.addField(bucket.upperBound.toGoString(), bucket.cumulativeCount)
                    }
                    point.addField("count", metric.sampleCount)
                    point.addField("sum", metric.sampleSum)
                }
            }

            writeApi.writePoint(point)
        }
    }
}
