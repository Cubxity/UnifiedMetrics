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

package dev.cubxity.plugins.metrics.bukkit

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.bukkit.bootstrap.UnifiedMetricsBukkitBootstrap
import dev.cubxity.plugins.metrics.bukkit.metric.events.player.EventsPlayerCollection
import dev.cubxity.plugins.metrics.bukkit.metric.events.server.EventsServerCollection
import dev.cubxity.plugins.metrics.bukkit.metric.server.player.ServerPlayerCollection
import dev.cubxity.plugins.metrics.bukkit.metric.server.plugin.ServerPluginCollection
import dev.cubxity.plugins.metrics.bukkit.metric.server.tick.ServerTickCollection
import dev.cubxity.plugins.metrics.bukkit.metric.server.world.ServerWorldCollection
import dev.cubxity.plugins.metrics.bukkit.metric.server.world.ServerWorldFSCollection
import dev.cubxity.plugins.metrics.core.plugin.CoreUnifiedMetricsPlugin
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
        bootstrap.server.servicesManager.register(UnifiedMetrics::class.java, api, bootstrap, ServicePriority.Normal)
    }

    override fun registerPlatformMetrics() {
        super.registerPlatformMetrics()

        apiProvider.metricsManager.apply {
            with(config.metrics.collectors) {
                if (server.player) registerCollection(ServerPlayerCollection(bootstrap))
                if (server.plugin) registerCollection(ServerPluginCollection(bootstrap))
                if (server.world) registerCollection(ServerWorldCollection(bootstrap))
                if (server.worldfs) registerCollection(ServerWorldFSCollection(bootstrap))
                if (server.tick) registerCollection(ServerTickCollection(bootstrap))
                if (events.player) registerCollection(EventsPlayerCollection(bootstrap))
                if (events.server) registerCollection(EventsServerCollection(bootstrap))
            }
        }
    }
}