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

package dev.cubxity.plugins.metrics.bungee.metric

import dev.cubxity.plugins.metrics.api.metric.Metric
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.bungee.bootstrap.UnifiedMetricsBungeeBootstrap
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.ProxyPingEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

@Suppress("UNUSED_PARAMETER")
class EventsMetric(private val bootstrap: UnifiedMetricsBungeeBootstrap) : Metric, Listener {
    private val joinCounter = Counter("minecraft_events_join_total")
    private val quitCounter = Counter("minecraft_events_quit_total")
    private val chatCounter = Counter("minecraft_events_chat_total")
    private val pingCounter = Counter("minecraft_events_ping_total")

    override val collectors: List<MetricCollector> =
        listOf(joinCounter, quitCounter, chatCounter, pingCounter)

    override fun initialize() {
        bootstrap.proxy.pluginManager.registerListener(bootstrap, this)
    }

    override fun dispose() {
        bootstrap.proxy.pluginManager.unregisterListener(this)
    }

    @EventHandler
    fun onJoin(event: PostLoginEvent) {
        joinCounter.inc()
    }

    @EventHandler
    fun onQuit(event: PlayerDisconnectEvent) {
        quitCounter.inc()
    }

    @EventHandler
    fun onChat(event: ChatEvent) {
        chatCounter.inc()
    }

    @EventHandler
    fun onPing(event: ProxyPingEvent) {
        pingCounter.inc()
    }
}