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

import com.charleskorn.kaml.Yaml
import dev.cubxity.plugins.metrics.api.metric.Metric
import dev.cubxity.plugins.metrics.api.metric.MetricsDriver
import dev.cubxity.plugins.metrics.api.metric.MetricsDriverFactory
import dev.cubxity.plugins.metrics.api.metric.MetricsManager
import dev.cubxity.plugins.metrics.common.plugin.UnifiedMetricsPlugin
import java.nio.file.Files
import kotlin.system.measureTimeMillis

class MetricsManagerImpl(private val plugin: UnifiedMetricsPlugin) : MetricsManager {
    private val driverDirectory = plugin.bootstrap.configDirectory.resolve("driver")
    private val metricDrivers: MutableMap<String, MetricsDriverFactory<Any>> = HashMap()
    private val _metrics: MutableList<Metric> = ArrayList()

    private var shouldInitialize: Boolean = false
    private var driver: MetricsDriver? = null

    override val metrics: List<Metric>
        get() = _metrics

    override fun initialize() {
        shouldInitialize = true

        val driverName = plugin.config.metrics.driver
        val factory = metricDrivers[driverName]

        Files.createDirectories(driverDirectory)

        if (factory !== null) {
            initializeDriver(driverName, factory)
        } else {
            plugin.bootstrap.logger.warn("Driver '$driverName' not found. Metrics will be enabled when the driver is loaded.")
        }
    }

    override fun registerMetric(metric: Metric) {
        try {
            metric.initialize()
            _metrics.add(metric)
        } catch (error: Throwable) {
            plugin.bootstrap.logger.warn("An error occurred whilst registering metric", error)
        }
    }

    override fun unregisterMetric(metric: Metric) {
        try {
            _metrics.remove(metric)
            metric.dispose()
        } catch (error: Throwable) {
            plugin.bootstrap.logger.warn("An error occurred whilst unregistering metric", error)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun registerDriver(name: String, factory: MetricsDriverFactory<out Any>) {
        metricDrivers[name] = factory as MetricsDriverFactory<Any>

        if (shouldInitialize && driver === null) {
            if (name == plugin.config.metrics.driver) {
                initializeDriver(name, factory)
            }
        }
    }

    override fun dispose() {
        shouldInitialize = false

        metrics.toList().forEach { unregisterMetric(it) }
        driver?.close()
        driver = null
    }

    private fun initializeDriver(name: String, factory: MetricsDriverFactory<Any>) {
        plugin.bootstrap.logger.info("Initializing driver '$name'.")
        val time = measureTimeMillis {
            try {
                val file = driverDirectory.toFile().resolve("$name.yml")

                val serializer = factory.configSerializer
                val config = when {
                    file.exists() -> Yaml.default.decodeFromString(serializer, file.readText())
                    else -> factory.defaultConfig
                }

                try {
                    file.writeText(Yaml.default.encodeToString(serializer, config))
                } catch (exception: Exception) {
                    plugin.apiProvider.logger.severe("An error occurred whilst saving driver config file ", exception)
                }

                val driver = factory.createDriver(plugin.apiProvider, config)
                driver.initialize()

                this.driver = driver
            } catch (error: Throwable) {
                plugin.apiProvider.logger.severe("An error occurred whilst initializing metrics driver $name", error)
            }
        }
        plugin.bootstrap.logger.info("Driver '$name' initialized ($time ms).")
    }
}