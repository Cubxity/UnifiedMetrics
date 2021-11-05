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

package dev.cubxity.plugins.metrics.bukkit.metric.events.server

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.bukkit.bootstrap.UnifiedMetricsBukkitBootstrap
import dev.cubxity.plugins.metrics.common.metric.Metrics
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

@Suppress("UNUSED_PARAMETER")
class EventsServerCollection(private val bootstrap: UnifiedMetricsBukkitBootstrap) : CollectorCollection, Listener {
    private val pingCounter = Counter(Metrics.Events.ServerPing) // TODO: is this async?

    override val collectors: List<Collector> = listOf(pingCounter)

    override val isAsync: Boolean
        get() = true

    override fun initialize() {
        bootstrap.server.pluginManager.registerEvents(this, bootstrap)
    }

    override fun dispose() {
        HandlerList.unregisterAll(this)
    }

    @EventHandler
    fun onPing(event: ServerListPingEvent) {
        pingCounter.inc()
    }
}