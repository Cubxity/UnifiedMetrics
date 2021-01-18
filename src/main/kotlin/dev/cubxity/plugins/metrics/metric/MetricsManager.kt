/*
 *     UnifiedMetrics is a fully-featured metrics collection plugin for Spigot.
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

package dev.cubxity.plugins.metrics.metric

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.InfluxDBClientFactory
import com.influxdb.client.InfluxDBClientOptions
import com.influxdb.client.domain.WritePrecision
import dev.cubxity.plugins.metrics.UnifiedMetrics
import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MetricsManager(private val plugin: UnifiedMetrics) {
    private val executor = Executors.newScheduledThreadPool(1)
    private var influxDBClient: InfluxDBClient? = null

    private var _metrics: MutableList<Metric<*>> = ArrayList()

    val metrics: List<Metric<*>>
        get() = _metrics

    fun start() {
        val config = plugin.metricsConfig.influx

        val options = InfluxDBClientOptions.builder()
            .url(config.url)
            .authenticate(config.username, config.password.toCharArray())
            .bucket(config.bucket)
            .org("-")
            .build()
        influxDBClient = InfluxDBClientFactory.create(options)

        val interval = config.interval.toLong()
        executor.scheduleAtFixedRate(MetricsRunnable(false), 0, interval, TimeUnit.SECONDS)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, MetricsRunnable(true), 0, interval * 20)
    }

    fun stop() {
        executor.shutdownNow()
        Bukkit.getScheduler().cancelTasks(plugin)
        influxDBClient = null

        metrics.forEach { unregisterMetric(it) }
    }

    fun registerMetric(metric: Metric<*>) {
        _metrics.add(metric)
    }

    fun unregisterMetric(metric: Metric<*>) {
        _metrics.remove(metric)
        metric.dispose(plugin)
    }

    private inner class MetricsRunnable(private val sync: Boolean) : Runnable {
        override fun run() {
            val client = influxDBClient ?: return
            client.writeApi.use { api ->
                for (metric in metrics) {
                    if (metric.isSync == sync) {
                        api.writeMeasurements(WritePrecision.MS, metric.getMeasurements(plugin))
                    }
                }
            }
        }
    }
}