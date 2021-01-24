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

package dev.cubxity.plugins.metrics.common.metric.influx

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.InfluxDBClientFactory
import com.influxdb.client.InfluxDBClientOptions
import dev.cubxity.plugins.metrics.api.metric.MetricsDriver
import dev.cubxity.plugins.metrics.api.metric.data.Point
import dev.cubxity.plugins.metrics.common.config.MetricsSpec
import dev.cubxity.plugins.metrics.common.plugin.UnifiedMetricsPlugin
import java.util.concurrent.TimeUnit
import com.influxdb.client.write.Point as InfluxPoint

class InfluxMetricsDriver(private val plugin: UnifiedMetricsPlugin) : MetricsDriver {
    private var influxDBClient: InfluxDBClient? = null

    override fun connect() {
        val config = plugin.config

        val options = InfluxDBClientOptions.builder()
            .url(config[MetricsSpec.InfluxSpec.url])
            .authenticate(
                config[MetricsSpec.InfluxSpec.username],
                config[MetricsSpec.InfluxSpec.password].toCharArray()
            )
            .bucket(config[MetricsSpec.InfluxSpec.bucket])
            .org("-")
            .build()

        influxDBClient = InfluxDBClientFactory.create(options)
    }

    override fun scheduleTasks() {
        val interval = plugin.config[MetricsSpec.interval]

        val scheduler = plugin.bootstrap.scheduler

        scheduler.asyncRepeating({
            // Async
            plugin.apiProvider.metricsManager.writeMetrics(false)

            // Sync
            scheduler.sync.execute {
                plugin.apiProvider.metricsManager.writeMetrics(true)
            }
        }, interval, TimeUnit.SECONDS)
    }

    override fun writePoints(points: List<Point>) {
        influxDBClient?.writeApi?.use { api ->
            for (point in points) {
                val influxPoint = InfluxPoint(point.name)
                influxPoint.addFields(point.fields)
                influxPoint.addTags(point.tags)

                api.writePoint(influxPoint)
            }
        }
    }

    override fun close() {
        influxDBClient?.close()
        influxDBClient = null
    }
}