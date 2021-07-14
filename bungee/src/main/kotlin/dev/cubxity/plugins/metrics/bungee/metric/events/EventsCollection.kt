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

package dev.cubxity.plugins.metrics.bungee.metric.events

import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.bungee.bootstrap.UnifiedMetricsBungeeBootstrap
import net.md_5.bungee.api.event.*
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

@Suppress("UNUSED_PARAMETER")
class EventsCollection(private val bootstrap: UnifiedMetricsBungeeBootstrap) : CollectorCollection, Listener {
    private val loginCounter = Counter("minecraft_events_login_total")
    private val joinCounter = Counter("minecraft_events_join_total")
    private val quitCounter = Counter("minecraft_events_quit_total")
    private val chatCounter = Counter("minecraft_events_chat_total")
    private val pingCounter = Counter("minecraft_events_ping_total")

    override val collectors: List<Collector> =
        listOf(loginCounter, joinCounter, quitCounter, chatCounter, pingCounter)

    override fun initialize() {
        bootstrap.proxy.pluginManager.registerListener(bootstrap, this)
    }

    override fun dispose() {
        bootstrap.proxy.pluginManager.unregisterListener(this)
    }

    @EventHandler
    fun onLogin(event: PreLoginEvent) {
        loginCounter.inc()
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