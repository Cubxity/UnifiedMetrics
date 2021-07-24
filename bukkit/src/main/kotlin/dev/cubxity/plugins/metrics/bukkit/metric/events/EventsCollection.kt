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

@file:Suppress("DEPRECATION") // Using Paper's event will cause incompatibility with non-paper bukkit/spigot servers

package dev.cubxity.plugins.metrics.bukkit.metric.events

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.api.metric.store.VolatileDoubleStore
import dev.cubxity.plugins.metrics.bukkit.bootstrap.UnifiedMetricsBukkitBootstrap
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.ServerListPingEvent

@Suppress("UNUSED_PARAMETER")
class EventsCollection(private val bootstrap: UnifiedMetricsBukkitBootstrap) : CollectorCollection, Listener {
    private val loginCounter = Counter("minecraft_events_login_total")
    private val joinCounter = Counter("minecraft_events_join_total", valueStoreFactory = VolatileDoubleStore)
    private val quitCounter = Counter("minecraft_events_quit_total", valueStoreFactory = VolatileDoubleStore)
    private val chatCounter = Counter("minecraft_events_chat_total")
    private val pingCounter = Counter("minecraft_events_ping_total") // TODO: is this async?

    override val collectors: List<Collector> =
        listOf(loginCounter, joinCounter, quitCounter, chatCounter, pingCounter)

    override val isAsync: Boolean
        get() = true

    override fun initialize() {
        bootstrap.server.pluginManager.registerEvents(this, bootstrap)
    }

    override fun dispose() {
        HandlerList.unregisterAll(this)
    }

    @EventHandler
    fun onLogin(event: AsyncPlayerPreLoginEvent) {
        loginCounter.inc()
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        joinCounter.inc()
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        quitCounter.inc()
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        chatCounter.inc()
    }

    @EventHandler
    fun onPing(event: ServerListPingEvent) {
        pingCounter.inc()
    }
}