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

package dev.cubxity.plugins.metrics.sponge.metric.tick

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Histogram
import dev.cubxity.plugins.metrics.api.metric.collector.MILLISECONDS_PER_SECOND
import dev.cubxity.plugins.metrics.api.metric.store.VolatileDoubleStore
import dev.cubxity.plugins.metrics.api.metric.store.VolatileLongStore
import dev.cubxity.plugins.metrics.common.metric.Metrics
import dev.cubxity.plugins.metrics.sponge.bootstrap.UnifiedMetricsSpongeBootstrap
import dev.cubxity.plugins.metrics.sponge.events.TickEndEvent
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener

class TickCollection(private val bootstrap: UnifiedMetricsSpongeBootstrap) : CollectorCollection {

    private val tickDuration = Histogram(
        Metrics.Server.TickDurationSeconds,
        sumStoreFactory = VolatileDoubleStore,
        countStoreFactory = VolatileLongStore
    )

    override val collectors: List<Collector> = listOf(tickDuration)

    override fun initialize() {
        Sponge.eventManager().registerListeners(bootstrap.container, this)
    }

    override fun dispose() {
        Sponge.eventManager().unregisterListeners(this)
    }

    @Listener
    fun onTickEnd(event: TickEndEvent) {
        onTick(event.duration / MILLISECONDS_PER_SECOND)
    }

    fun onTick(duration: Double) {
        tickDuration += duration
    }
}