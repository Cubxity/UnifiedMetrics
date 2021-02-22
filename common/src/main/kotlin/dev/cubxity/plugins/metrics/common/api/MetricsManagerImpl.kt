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

package dev.cubxity.plugins.metrics.common.api

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.Feature
import com.uchuhimo.konf.source.toml
import com.uchuhimo.konf.source.toml.toToml
import dev.cubxity.plugins.metrics.api.metric.Metric
import dev.cubxity.plugins.metrics.api.metric.MetricsDriver
import dev.cubxity.plugins.metrics.api.metric.MetricsDriverFactory
import dev.cubxity.plugins.metrics.api.metric.MetricsManager
import dev.cubxity.plugins.metrics.api.metric.data.Point
import dev.cubxity.plugins.metrics.common.config.MetricsSpec
import dev.cubxity.plugins.metrics.common.plugin.UnifiedMetricsPlugin
import java.nio.file.Files

class MetricsManagerImpl(private val plugin: UnifiedMetricsPlugin) : MetricsManager {
    private val driverDirectory = plugin.bootstrap.configDirectory.resolve("driver")
    private val metricDrivers: MutableMap<String, MetricsDriverFactory> = HashMap()
    private val _metrics: MutableList<Metric<*>> = ArrayList()

    private var shouldInitialize: Boolean = false
    private var driver: MetricsDriver? = null

    override val metrics: List<Metric<*>>
        get() = _metrics

    override fun initialize() {
        shouldInitialize = true

        val driverName = plugin.config[MetricsSpec.driver]
        val factory = metricDrivers[driverName]

        Files.createDirectories(driverDirectory)

        if (factory !== null) {
            plugin.bootstrap.logger.warn("Initializing driver '$driverName'.")
            initializeDriver(driverName, factory)
        } else {
            plugin.bootstrap.logger.warn("Driver '$driverName' not found. Metrics will be enabled when the driver is loaded.")
        }
    }

    override fun registerMetric(metric: Metric<*>) {
        metric.initialize()
        _metrics.add(metric)
    }

    override fun unregisterMetric(metric: Metric<*>) {
        _metrics.remove(metric)
        metric.dispose()
    }

    override fun registerDriver(name: String, factory: MetricsDriverFactory) {
        metricDrivers[name] = factory

        if (shouldInitialize && driver === null) {
            val driverName = plugin.config[MetricsSpec.driver]

            if (name == driverName) {
                plugin.bootstrap.logger.warn("Initializing driver '$driverName'.")
                initializeDriver(name, factory)
            }
        }
    }

    override fun serializeMetrics(isSync: Boolean): List<Point> {
        return metrics.filter { it.isSync == isSync }
            .flatMap { it.getMeasurements(plugin.apiProvider) }
            .map { it.serialize().tag("server", plugin.apiProvider.serverName) }
    }

    override fun dispose() {
        shouldInitialize = false

        metrics.toList().forEach { unregisterMetric(it) }
        driver?.close()
        driver = null
    }

    private fun initializeDriver(name: String, factory: MetricsDriverFactory) {
        val file = driverDirectory.toFile().resolve("$name.toml")
        val config = Config { factory.registerConfig(this) }
            .enable(Feature.OPTIONAL_SOURCE_BY_DEFAULT)
            .from.toml.file(file)

        config.toToml.toFile(file)

        val driver = factory.createDriver(plugin.apiProvider, config)

        driver.connect()
        driver.scheduleTasks()

        this.driver = driver
    }
}