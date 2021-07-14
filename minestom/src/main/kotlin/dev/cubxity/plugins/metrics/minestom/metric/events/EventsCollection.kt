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

package dev.cubxity.plugins.metrics.minestom.metric.events

import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.server.ServerListPingEvent

class EventsCollection : CollectorCollection {
    private val loginCounter = Counter("minecraft_events_login_total")
    private val joinCounter = Counter("minecraft_events_join_total")
    private val quitCounter = Counter("minecraft_events_quit_total")
    private val chatCounter = Counter("minecraft_events_chat_total")
    private val pingCounter = Counter("minecraft_events_ping_total")

    private val loginListener = EventListener.of(AsyncPlayerPreLoginEvent::class.java) { loginCounter.inc() }
    private val joinListener = EventListener.of(PlayerLoginEvent::class.java) { joinCounter.inc() }
    private val disconnectListener = EventListener.of(PlayerDisconnectEvent::class.java) { quitCounter.inc() }
    private val chatListener = EventListener.of(PlayerChatEvent::class.java) { chatCounter.inc() }
    private val pingListener = EventListener.of(ServerListPingEvent::class.java) { pingCounter.inc() }

    override val collectors: List<Collector> =
        listOf(joinCounter, quitCounter, chatCounter, pingCounter)

    override fun initialize() {
        with(MinecraftServer.getGlobalEventHandler()) {
            addListener(loginListener)
            addListener(joinListener)
            addListener(disconnectListener)
            addListener(chatListener)
            addListener(pingListener)
        }
    }

    override fun dispose() {
        with(MinecraftServer.getGlobalEventHandler()) {
            removeListener(loginListener)
            removeListener(joinListener)
            removeListener(disconnectListener)
            removeListener(chatListener)
            removeListener(pingListener)
        }
    }
}