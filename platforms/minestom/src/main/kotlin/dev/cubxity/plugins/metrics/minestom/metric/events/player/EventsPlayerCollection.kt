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

package dev.cubxity.plugins.metrics.minestom.metric.events.player

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.common.metric.Metrics
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoginEvent

class EventsPlayerCollection : CollectorCollection {
    // TODO: are these events async?
    private val loginCounter = Counter(Metrics.Events.PlayerLogin)
    private val joinCounter = Counter(Metrics.Events.PlayerJoin)
    private val quitCounter = Counter(Metrics.Events.PlayerQuit)
    private val chatCounter = Counter(Metrics.Events.PlayerChat)

    private val loginListener = EventListener.of(AsyncPlayerPreLoginEvent::class.java) { loginCounter.inc() }
    private val joinListener = EventListener.of(PlayerLoginEvent::class.java) { joinCounter.inc() }
    private val disconnectListener = EventListener.of(PlayerDisconnectEvent::class.java) { quitCounter.inc() }
    private val chatListener = EventListener.of(PlayerChatEvent::class.java) { chatCounter.inc() }

    override val collectors: List<Collector> = listOf(joinCounter, quitCounter, chatCounter)

    override fun initialize() {
        with(MinecraftServer.getGlobalEventHandler()) {
            addListener(loginListener)
            addListener(joinListener)
            addListener(disconnectListener)
            addListener(chatListener)
        }
    }

    override fun dispose() {
        with(MinecraftServer.getGlobalEventHandler()) {
            removeListener(loginListener)
            removeListener(joinListener)
            removeListener(disconnectListener)
            removeListener(chatListener)
        }
    }
}