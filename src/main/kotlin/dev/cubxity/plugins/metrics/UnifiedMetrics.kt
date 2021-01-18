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

package dev.cubxity.plugins.metrics

import dev.cubxity.plugins.metrics.metric.*
import org.bukkit.plugin.java.JavaPlugin

@Suppress("MemberVisibilityCanBePrivate")
class UnifiedMetrics : JavaPlugin() {
    private var _metricsConfig: UnifiedMetricsConfig? = null

    val metricsManager = MetricsManager(this)

    val metricsConfig: UnifiedMetricsConfig
        get() = _metricsConfig ?: error("The plugin has not been initialized")

    override fun onEnable() {
        config.options().copyDefaults(true)
        saveDefaultConfig()

        _metricsConfig = UnifiedMetricsConfig(config)

        if (metricsConfig.influx.isEnabled) {
            metricsManager.registerMetric(JVMMetric())
            metricsManager.registerMetric(MemoryMetric())
            metricsManager.registerMetric(ServerMetric())
            metricsManager.registerMetric(TPSMetric())
            metricsManager.registerMetric(WorldMetric())
            metricsManager.start()
        }
    }

    override fun onDisable() {
        if (metricsConfig.influx.isEnabled) {
            metricsManager.stop()
        }
        _metricsConfig = null
    }
}