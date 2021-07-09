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

package dev.cubxity.plugins.metrics.velocity.metric

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.PreLoginEvent
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import dev.cubxity.plugins.metrics.api.metric.Metric
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.velocity.bootstrap.UnifiedMetricsVelocityBootstrap

@Suppress("UNUSED_PARAMETER")
class EventsMetric(private val bootstrap: UnifiedMetricsVelocityBootstrap) : Metric {
    private val loginCounter = Counter("minecraft_events_login_total")
    private val joinCounter = Counter("minecraft_events_join_total")
    private val quitCounter = Counter("minecraft_events_quit_total")
    private val chatCounter = Counter("minecraft_events_chat_total")
    private val pingCounter = Counter("minecraft_events_ping_total")

    override val collectors: List<MetricCollector> =
        listOf(loginCounter, joinCounter, quitCounter, chatCounter, pingCounter)

    override fun initialize() {
        bootstrap.server.eventManager.register(bootstrap, this)
    }

    override fun dispose() {
        bootstrap.server.eventManager.unregisterListener(bootstrap, this)
    }


    @Subscribe
    fun onLogin(event: PreLoginEvent) {
        joinCounter.inc()
    }

    @Subscribe
    fun onConnect(event: PlayerChooseInitialServerEvent) {
        joinCounter.inc()
    }

    @Subscribe
    fun onDisconnect(event: DisconnectEvent) {
        quitCounter.inc()
    }

    @Subscribe
    fun onChat(event: PlayerChatEvent) {
        chatCounter.inc()
    }

    @Subscribe
    fun onPing(event: ProxyPingEvent) {
        pingCounter.dec()
    }
}