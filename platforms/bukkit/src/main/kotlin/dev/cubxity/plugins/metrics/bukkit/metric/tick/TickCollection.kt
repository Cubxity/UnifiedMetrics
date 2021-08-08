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

package dev.cubxity.plugins.metrics.bukkit.metric.tick

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Histogram
import dev.cubxity.plugins.metrics.api.metric.store.VolatileDoubleStore
import dev.cubxity.plugins.metrics.api.metric.store.VolatileLongStore
import dev.cubxity.plugins.metrics.bukkit.bootstrap.UnifiedMetricsBukkitBootstrap
import dev.cubxity.plugins.metrics.bukkit.util.classExists
import dev.cubxity.plugins.metrics.common.metric.Metrics

class TickCollection(bootstrap: UnifiedMetricsBukkitBootstrap) : CollectorCollection {
    private val reporter = if (classExists("com.destroystokyo.paper.event.server.ServerTickStartEvent")) {
        PaperTickReporter(this, bootstrap)
    } else {
        BukkitTickReporter(this, bootstrap)
    }

    // The callback is called from a single thread
    private val tickDuration = Histogram(
        Metrics.Server.TickDurationSeconds,
        sumStoreFactory = VolatileDoubleStore,
        countStoreFactory = VolatileLongStore
    )

    override val collectors: List<Collector> = listOf(tickDuration)

    override val isAsync: Boolean
        get() = true

    override fun initialize() {
        reporter.initialize()
    }

    override fun dispose() {
        reporter.dispose()
    }

    /**
     * @param duration tick duration in seconds
     */
    fun onTick(duration: Double) {
        tickDuration += duration
    }
}