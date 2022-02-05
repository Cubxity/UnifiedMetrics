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

package dev.cubxity.plugins.metrics.minestom.metric.tick

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Histogram
import dev.cubxity.plugins.metrics.api.metric.collector.MILLISECONDS_PER_SECOND
import dev.cubxity.plugins.metrics.api.metric.store.VolatileDoubleStore
import dev.cubxity.plugins.metrics.api.metric.store.VolatileLongStore
import dev.cubxity.plugins.metrics.common.metric.Metrics
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventListener
import net.minestom.server.event.server.ServerTickMonitorEvent

class TickCollection : CollectorCollection {
    // The callback is called from a single thread
    private val tickDuration = Histogram(
        Metrics.Server.TickDurationSeconds,
        sumStoreFactory = VolatileDoubleStore,
        countStoreFactory = VolatileLongStore
    )

    private val listener = EventListener.of(ServerTickMonitorEvent::class.java) { event ->
        tickDuration += event.tickMonitor.tickTime / MILLISECONDS_PER_SECOND
    }

    override val collectors: List<Collector> = listOf(tickDuration)

    override fun initialize() {
        MinecraftServer.getGlobalEventHandler().addListener(listener)
    }

    override fun dispose() {
        MinecraftServer.getGlobalEventHandler().removeListener(listener)
    }
}