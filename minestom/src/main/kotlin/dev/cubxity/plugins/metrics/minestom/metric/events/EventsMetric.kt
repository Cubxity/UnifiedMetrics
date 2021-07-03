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

package dev.cubxity.plugins.metrics.minestom.metric.events

import dev.cubxity.plugins.metrics.api.metric.Metric
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.server.ServerListPingEvent

class EventsMetric : Metric {
    private val joinCounter = Counter("minecraft_events_join_total")
    private val quitCounter = Counter("minecraft_events_quit_total")
    private val chatCounter = Counter("minecraft_events_chat_total")
    private val pingCounter = Counter("minecraft_events_ping_total")

    private val loginListener = EventListener.of(PlayerLoginEvent::class.java) { joinCounter.inc() }
    private val disconnectListener = EventListener.of(PlayerDisconnectEvent::class.java) { quitCounter.inc() }
    private val chatListener = EventListener.of(PlayerChatEvent::class.java) { chatCounter.inc() }
    private val pingListener = EventListener.of(ServerListPingEvent::class.java) { pingCounter.inc() }

    override val collectors: List<MetricCollector> =
        listOf(joinCounter, quitCounter, chatCounter, pingCounter)

    override fun initialize() {
        with(MinecraftServer.getGlobalEventHandler()) {
            addListener(loginListener)
            addListener(disconnectListener)
            addListener(chatListener)
            addListener(pingListener)
        }
    }

    override fun dispose() {
        with(MinecraftServer.getGlobalEventHandler()) {
            removeListener(loginListener)
            removeListener(disconnectListener)
            removeListener(chatListener)
            removeListener(pingListener)
        }
    }
}