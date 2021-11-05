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

package dev.cubxity.plugins.metrics.bungee.metric.events.server

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.bungee.bootstrap.UnifiedMetricsBungeeBootstrap
import dev.cubxity.plugins.metrics.common.metric.Metrics
import net.md_5.bungee.api.event.ProxyPingEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

@Suppress("UNUSED_PARAMETER")
class EventsServerCollection(private val bootstrap: UnifiedMetricsBungeeBootstrap) : CollectorCollection, Listener {
    private val pingCounter = Counter(Metrics.Events.ServerPing)

    override val collectors: List<Collector> = listOf(pingCounter)

    override fun initialize() {
        bootstrap.proxy.pluginManager.registerListener(bootstrap, this)
    }

    override fun dispose() {
        bootstrap.proxy.pluginManager.unregisterListener(this)
    }

    @EventHandler
    fun onPing(event: ProxyPingEvent) {
        pingCounter.inc()
    }
}