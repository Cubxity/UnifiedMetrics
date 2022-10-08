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

package dev.cubxity.metrics.sponge.metric.events

import dev.cubxity.metrics.sponge.bootstrap.UnifiedMetricsSpongeBootstrap
import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.message.PlayerChatEvent
import org.spongepowered.api.event.network.ServerSideConnectionEvent.*
import org.spongepowered.api.event.server.ClientPingServerEvent

class EventsCollection(private val bootstrap: UnifiedMetricsSpongeBootstrap) : CollectorCollection {

    private val loginCounter = Counter("minecraft_events_login_total")
    private val joinCounter = Counter("minecraft_events_join_total")
    private val quitCounter = Counter("minecraft_events_quit_total")
    private val chatCounter = Counter("minecraft_events_chat_total")
    private val pingCounter = Counter("minecraft_events_ping_total")

    override val collectors: List<Collector> = listOf(loginCounter, joinCounter, quitCounter, chatCounter, pingCounter)

    override fun initialize() {
        Sponge.eventManager().registerListeners(bootstrap.container, this)
    }

    override fun dispose() {
        Sponge.eventManager().unregisterListeners(this)
    }

    @Listener
    fun onLogin(event: Join) {
        joinCounter.inc()
    }

    @Listener
    fun onConnect(event: Login) {
        loginCounter.inc()
    }


    @Listener
    fun onDisconnect(event: Disconnect) {
        println("disconnect")
        quitCounter.inc()
    }

    @Listener
    fun onChat(event: PlayerChatEvent) {
        chatCounter.inc()
    }

    @Listener
    fun onPing(event: ClientPingServerEvent) {
        pingCounter.inc()
    }
}