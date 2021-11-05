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

package dev.cubxity.plugins.metrics.velocity.metric.events.server

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.common.metric.Metrics
import dev.cubxity.plugins.metrics.velocity.bootstrap.UnifiedMetricsVelocityBootstrap

@Suppress("UNUSED_PARAMETER")
class EventsServerCollection(private val bootstrap: UnifiedMetricsVelocityBootstrap) : CollectorCollection {
    private val pingCounter = Counter(Metrics.Events.ServerPing)

    override val collectors: List<Collector> = listOf(pingCounter)

    override fun initialize() {
        bootstrap.server.eventManager.register(bootstrap, this)
    }

    override fun dispose() {
        bootstrap.server.eventManager.unregisterListener(bootstrap, this)
    }

    @Subscribe
    fun onPing(event: ProxyPingEvent) {
        pingCounter.inc()
    }
}