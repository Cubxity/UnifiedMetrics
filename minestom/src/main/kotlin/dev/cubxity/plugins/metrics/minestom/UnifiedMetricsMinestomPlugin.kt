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

package dev.cubxity.plugins.metrics.minestom

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.core.plugin.CoreUnifiedMetricsPlugin
import dev.cubxity.plugins.metrics.minestom.bootstrap.UnifiedMetricsMinestomBootstrap
import dev.cubxity.plugins.metrics.minestom.metric.events.EventsMetric
import dev.cubxity.plugins.metrics.minestom.metric.server.ServerMetric
import dev.cubxity.plugins.metrics.minestom.metric.tick.TickMetric
import dev.cubxity.plugins.metrics.minestom.metric.world.WorldMetric

class UnifiedMetricsMinestomPlugin(
    override val bootstrap: UnifiedMetricsMinestomBootstrap
) : CoreUnifiedMetricsPlugin() {
    override fun registerPlatformService(api: UnifiedMetrics) {

    }

    override fun registerPlatformMetrics() {
        super.registerPlatformMetrics()

        apiProvider.metricsManager.apply {
            with(config.metrics.collectors) {
                if (server) registerMetric(ServerMetric())
                if (world) registerMetric(WorldMetric())
                if (tick) registerMetric(TickMetric())
                if (events) registerMetric(EventsMetric())
            }
        }
    }
}