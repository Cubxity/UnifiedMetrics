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

package dev.cubxity.plugins.metrics.bukkit

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.bukkit.bootstrap.UnifiedMetricsBukkitBootstrap
import dev.cubxity.plugins.metrics.bukkit.metric.EventsMetric
import dev.cubxity.plugins.metrics.bukkit.metric.ServerMetric
import dev.cubxity.plugins.metrics.bukkit.metric.TPSMetric
import dev.cubxity.plugins.metrics.bukkit.metric.WorldMetric
import dev.cubxity.plugins.metrics.core.plugin.CoreUnifiedMetricsPlugin
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import java.util.concurrent.Executors

class UnifiedMetricsBukkitPlugin(
    override val bootstrap: UnifiedMetricsBukkitBootstrap
) : CoreUnifiedMetricsPlugin() {
    private val executor = Executors.newScheduledThreadPool(1)

    override fun disable() {
        executor.shutdownNow()
        super.disable()
    }

    override fun registerPlatformService(api: UnifiedMetrics) {
        Bukkit.getServicesManager().register(UnifiedMetrics::class.java, api, bootstrap, ServicePriority.Normal)
    }

    override fun registerPlatformMetrics() {
        super.registerPlatformMetrics()

        apiProvider.metricsManager.apply {
            registerMetric(EventsMetric(bootstrap))
            registerMetric(ServerMetric())
            registerMetric(TPSMetric(bootstrap))
            registerMetric(WorldMetric())
        }
    }
}