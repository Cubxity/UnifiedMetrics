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

package dev.cubxity.plugins.metrics.minestom

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.core.plugin.CoreUnifiedMetricsPlugin
import dev.cubxity.plugins.metrics.minestom.bootstrap.UnifiedMetricsMinestomBootstrap
import dev.cubxity.plugins.metrics.minestom.metric.events.EventsCollection
import dev.cubxity.plugins.metrics.minestom.metric.server.ServerCollection
import dev.cubxity.plugins.metrics.minestom.metric.tick.TickCollection
import dev.cubxity.plugins.metrics.minestom.metric.world.WorldCollection

class UnifiedMetricsMinestomPlugin(
    override val bootstrap: UnifiedMetricsMinestomBootstrap
) : CoreUnifiedMetricsPlugin() {
    override fun registerPlatformService(api: UnifiedMetrics) {

    }

    override fun registerPlatformMetrics() {
        super.registerPlatformMetrics()

        apiProvider.metricsManager.apply {
            with(config.metrics.collectors) {
                if (server) registerCollection(ServerCollection())
                if (world) registerCollection(WorldCollection())
                if (tick) registerCollection(TickCollection())
                if (events) registerCollection(EventsCollection())
            }
        }
    }
}