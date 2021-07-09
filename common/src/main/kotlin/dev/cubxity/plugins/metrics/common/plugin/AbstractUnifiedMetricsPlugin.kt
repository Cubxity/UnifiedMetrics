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

package dev.cubxity.plugins.metrics.common.plugin

import com.charleskorn.kaml.Yaml
import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.api.UnifiedMetricsProvider
import dev.cubxity.plugins.metrics.common.api.UnifiedMetricsApiProvider
import dev.cubxity.plugins.metrics.common.config.UnifiedMetricsConfig
import dev.cubxity.plugins.metrics.common.metric.system.SystemMetric
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.nio.file.Files

abstract class AbstractUnifiedMetricsPlugin : UnifiedMetricsPlugin {
    private var _config: UnifiedMetricsConfig? = null
    private var _apiProvider: UnifiedMetricsApiProvider? = null

    override val config: UnifiedMetricsConfig
        get() = _config ?: error("The UnifiedMetrics plugin is not loaded.")

    override val apiProvider: UnifiedMetricsApiProvider
        get() = _apiProvider ?: error("The UnifiedMetrics plugin is not loaded.")

    open fun enable() {
        Files.createDirectories(bootstrap.dataDirectory)
        Files.createDirectories(bootstrap.configDirectory)

        _config = loadConfig()

        try {
            saveConfig()
        } catch (exception: Exception) {
            apiProvider.logger.severe("An error occurred whilst saving plugin config file", exception)
        }

        _apiProvider = UnifiedMetricsApiProvider(this)
        UnifiedMetricsProvider.register(apiProvider)
        registerPlatformService(apiProvider)

        if (config.metrics.enabled) {
            registerMetricsDrivers()
            registerPlatformMetrics()
            apiProvider.metricsManager.initialize()
        }
    }

    open fun disable() {
        if (config.metrics.enabled) {
            apiProvider.metricsManager.dispose()
        }

        UnifiedMetricsProvider.unregister()
        _apiProvider = null
    }

    abstract fun registerPlatformService(api: UnifiedMetrics)

    open fun registerMetricsDrivers() {

    }

    open fun registerPlatformMetrics() {
        apiProvider.metricsManager.apply {
            if (config.metrics.collectors.system) {
                registerMetric(SystemMetric())
            }
        }
    }

    private fun loadConfig(): UnifiedMetricsConfig {
        val file = bootstrap.configDirectory.toFile().resolve("config.yml")

        return when {
            file.exists() -> Yaml.default.decodeFromString(file.readText())
            else -> UnifiedMetricsConfig()
        }
    }

    private fun saveConfig() {
        val file = bootstrap.configDirectory.toFile().resolve("config.yml")
        file.writeText(Yaml.default.encodeToString(config))
    }
}