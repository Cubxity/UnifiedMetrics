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

package dev.cubxity.plugins.metrics.velocity

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.core.plugin.CoreUnifiedMetricsPlugin
import dev.cubxity.plugins.metrics.velocity.bootstrap.UnifiedMetricsVelocityBootstrap
import dev.cubxity.plugins.metrics.velocity.metric.events.player.EventsPlayerCollection
import dev.cubxity.plugins.metrics.velocity.metric.events.server.EventsServerCollection
import dev.cubxity.plugins.metrics.velocity.metric.server.player.ServerPlayerCollection
import dev.cubxity.plugins.metrics.velocity.metric.server.plugin.ServerPluginCollection

class UnifiedMetricsVelocityPlugin(
    override val bootstrap: UnifiedMetricsVelocityBootstrap
) : CoreUnifiedMetricsPlugin() {
    override fun registerPlatformService(api: UnifiedMetrics) {
        // Velocity doesn't have a service manager
    }

    override fun registerPlatformMetrics() {
        super.registerPlatformMetrics()

        apiProvider.metricsManager.apply {
            with(config.metrics.collectors) {
                if (server.player) registerCollection(ServerPlayerCollection(bootstrap))
                if (server.plugin) registerCollection(ServerPluginCollection(bootstrap))
                if (events.player) registerCollection(EventsPlayerCollection(bootstrap))
                if (events.server) registerCollection(EventsServerCollection(bootstrap))
            }
        }
    }
}