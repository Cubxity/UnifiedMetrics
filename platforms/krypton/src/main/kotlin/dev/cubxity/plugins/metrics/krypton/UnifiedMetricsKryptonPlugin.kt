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

package dev.cubxity.plugins.metrics.krypton

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.core.plugin.CoreUnifiedMetricsPlugin
import dev.cubxity.plugins.metrics.krypton.bootstrap.UnifiedMetricsKryptonBootstrap
import dev.cubxity.plugins.metrics.krypton.metric.events.EventsCollection
import dev.cubxity.plugins.metrics.krypton.metric.server.ServerCollection
import dev.cubxity.plugins.metrics.krypton.metric.tick.TickCollection
import dev.cubxity.plugins.metrics.krypton.metric.world.WorldCollection
import org.kryptonmc.api.service.register

class UnifiedMetricsKryptonPlugin(override val bootstrap: UnifiedMetricsKryptonBootstrap) : CoreUnifiedMetricsPlugin() {
    override fun registerPlatformService(api: UnifiedMetrics) {
        bootstrap.server.servicesManager.register(bootstrap, api)
    }

    override fun registerPlatformMetrics() {
        super.registerPlatformMetrics()

        apiProvider.metricsManager.apply {
            with(config.metrics.collectors) {
                if (server) registerCollection(ServerCollection(bootstrap))
                if (world) registerCollection(WorldCollection(bootstrap))
                if (tick) registerCollection(TickCollection(bootstrap))
                if (events) registerCollection(EventsCollection(bootstrap))
            }
        }
    }
}
