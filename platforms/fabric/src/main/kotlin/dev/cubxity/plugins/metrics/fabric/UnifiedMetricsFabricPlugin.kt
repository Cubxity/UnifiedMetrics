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

package dev.cubxity.plugins.metrics.fabric

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.core.plugin.CoreUnifiedMetricsPlugin
import dev.cubxity.plugins.metrics.fabric.bootstrap.UnifiedMetricsFabricBootstrap
import dev.cubxity.plugins.metrics.fabric.metrics.events.player.EventsPlayerCollection
import dev.cubxity.plugins.metrics.fabric.metrics.events.server.EventsServerCollection
import dev.cubxity.plugins.metrics.fabric.metrics.server.player.ServerPlayerCollection
import dev.cubxity.plugins.metrics.fabric.metrics.server.plugin.ServerPluginCollection
import dev.cubxity.plugins.metrics.fabric.metrics.server.tick.ServerTickCollection
import dev.cubxity.plugins.metrics.fabric.metrics.server.world.ServerWorldCollection

class UnifiedMetricsFabricPlugin(
    override val bootstrap: UnifiedMetricsFabricBootstrap
): CoreUnifiedMetricsPlugin() {

    override fun registerPlatformService(api: UnifiedMetrics) {

    }

    override fun registerPlatformMetrics() {
        super.registerPlatformMetrics()

        apiProvider.metricsManager.apply {
            with(config.metrics.collectors) {
                if (server.player) registerCollection(ServerPlayerCollection(bootstrap))
                if (server.plugin) registerCollection(ServerPluginCollection())
                if (server.world) registerCollection(ServerWorldCollection(bootstrap))
                if (server.tick) registerCollection(ServerTickCollection())
                if (events.player) registerCollection(EventsPlayerCollection())
                if (events.server) registerCollection(EventsServerCollection())
            }
        }
    }
}