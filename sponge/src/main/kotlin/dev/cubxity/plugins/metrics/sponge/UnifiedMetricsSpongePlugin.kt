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

package dev.cubxity.plugins.metrics.sponge

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.sponge.bootstrap.UnifiedMetricsSpongeBootstrap
import dev.cubxity.plugins.metrics.sponge.metric.EventsMetric
import dev.cubxity.plugins.metrics.sponge.metric.server.ServerMetric
import dev.cubxity.plugins.metrics.sponge.metric.tick.TickMetric
import dev.cubxity.plugins.metrics.sponge.metric.world.WorldMetric
import dev.cubxity.plugins.metrics.core.plugin.CoreUnifiedMetricsPlugin
import org.spongepowered.api.Sponge
import java.util.concurrent.Executors

class UnifiedMetricsSpongePlugin(
    override val bootstrap: UnifiedMetricsSpongeBootstrap
) : CoreUnifiedMetricsPlugin() {
    private val executor = Executors.newScheduledThreadPool(1)

    override fun disable() {
        executor.shutdownNow()
        super.disable()
    }

    override fun registerPlatformService(api: UnifiedMetrics) {
        Sponge.getServiceManager().setProvider(this, UnifiedMetrics::class.java, api)
    }

    override fun registerPlatformMetrics() {
        super.registerPlatformMetrics()

        apiProvider.metricsManager.apply {
            registerMetric(ServerMetric())
            registerMetric(WorldMetric())
            registerMetric(TickMetric())
            registerMetric(EventsMetric(bootstrap))
        }
    }
}