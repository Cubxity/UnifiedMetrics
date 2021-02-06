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

package dev.cubxity.plugins.metrics.common.plugin

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.Feature
import com.uchuhimo.konf.source.toml
import com.uchuhimo.konf.source.toml.toToml
import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.api.UnifiedMetricsProvider
import dev.cubxity.plugins.metrics.common.api.UnifiedMetricsApiProvider
import dev.cubxity.plugins.metrics.common.config.MetricsSpec
import dev.cubxity.plugins.metrics.common.config.ServerSpec
import dev.cubxity.plugins.metrics.common.metric.JVMMetric
import dev.cubxity.plugins.metrics.common.metric.MemoryMetric
import java.nio.file.Files

abstract class AbstractUnifiedMetricsPlugin : UnifiedMetricsPlugin {
    private var _config: Config? = null
    private var _apiProvider: UnifiedMetricsApiProvider? = null

    override val config: Config
        get() = _config ?: error("The UnifiedMetrics plugin is not loaded.")

    override val apiProvider: UnifiedMetricsApiProvider
        get() = _apiProvider ?: error("The UnifiedMetrics plugin is not loaded.")

    open fun enable() {
        Files.createDirectories(bootstrap.dataDirectory)
        Files.createDirectories(bootstrap.configDirectory)

        _config = loadConfig()
        saveConfig()

        _apiProvider = UnifiedMetricsApiProvider(this)
        UnifiedMetricsProvider.register(apiProvider)
        registerPlatformService(apiProvider)

        if (config[MetricsSpec.enabled]) {
            registerMetricsDrivers()
            registerPlatformMetrics()
            apiProvider.metricsManager.initialize()
        }
    }

    open fun disable() {
        if (config[MetricsSpec.enabled]) {
            apiProvider.metricsManager.dispose()
        }

        UnifiedMetricsProvider.unregister()
        _apiProvider = null

        bootstrap.scheduler.shutdown()
    }

    abstract fun registerPlatformService(api: UnifiedMetrics)

    open fun registerMetricsDrivers() {

    }

    open fun registerPlatformMetrics() {
        apiProvider.metricsManager.apply {
            registerMetric(JVMMetric())
            registerMetric(MemoryMetric())
        }
    }

    private fun loadConfig(): Config {
        val config = Config {
            addSpec(ServerSpec)
            addSpec(MetricsSpec)
        }
        val file = bootstrap.configDirectory.toFile().resolve("config.toml")
        return config.enable(Feature.OPTIONAL_SOURCE_BY_DEFAULT)
            .from.toml.file(file)
    }

    private fun saveConfig() {
        val file = bootstrap.configDirectory.toFile().resolve("config.toml")
        config.toToml.toFile(file)
    }
}