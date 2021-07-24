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

package dev.cubxity.plugins.metrics.common.api

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import dev.cubxity.plugins.metrics.api.metric.MetricsDriver
import dev.cubxity.plugins.metrics.api.metric.MetricsDriverFactory
import dev.cubxity.plugins.metrics.api.metric.MetricsManager
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.collect
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.api.util.fastForEach
import dev.cubxity.plugins.metrics.common.plugin.UnifiedMetricsPlugin
import kotlinx.coroutines.withContext
import java.nio.file.Files
import kotlin.system.measureTimeMillis

class MetricsManagerImpl(private val plugin: UnifiedMetricsPlugin) : MetricsManager {
    private val yaml = Yaml(configuration = YamlConfiguration(strictMode = false))
    private val driverDirectory = plugin.bootstrap.configDirectory.resolve("driver")

    private val metricDrivers: MutableMap<String, MetricsDriverFactory<Any>> = HashMap()
    private val _collections: MutableList<CollectorCollection> = ArrayList()

    private var shouldInitialize: Boolean = false
    private var driver: MetricsDriver? = null

    override val collections: List<CollectorCollection>
        get() = _collections

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

    override fun registerCollection(collection: CollectorCollection) {
        try {
            collection.initialize()
            _collections.add(collection)
        } catch (error: Throwable) {
            plugin.bootstrap.logger.warn("An error occurred whilst registering metric", error)
        }
    }

    override fun unregisterCollection(collection: CollectorCollection) {
        try {
            _collections.remove(collection)
            collection.dispose()
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

    override suspend fun collect(): List<Metric> {
        val list = ArrayList<Metric>()
        withContext(plugin.apiProvider.dispatcher) {
            collections.fastForEach { collection ->
                if (!collection.isAsync) {
                    list.addAll(collection.collect())
                }
            }
        }
        collections.fastForEach { collection ->
            if (collection.isAsync) {
                list.addAll(collection.collect())
            }
        }
        return list
    }

    override fun dispose() {
        shouldInitialize = false

        collections.toList().fastForEach { collection ->
            unregisterCollection(collection)
        }

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
                    file.exists() -> yaml.decodeFromString(serializer, file.readText())
                    else -> factory.defaultConfig
                }

                try {
                    file.writeText(yaml.encodeToString(serializer, config))
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