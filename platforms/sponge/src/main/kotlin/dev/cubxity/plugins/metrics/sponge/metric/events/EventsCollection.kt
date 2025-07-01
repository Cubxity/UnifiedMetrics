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

package dev.cubxity.plugins.metrics.sponge.metric.events

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.api.metric.store.VolatileDoubleStore
import dev.cubxity.plugins.metrics.common.metric.Metrics
import dev.cubxity.plugins.metrics.sponge.bootstrap.UnifiedMetricsSpongeBootstrap
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.message.PlayerChatEvent
import org.spongepowered.api.event.network.ServerSideConnectionEvent.*
import org.spongepowered.api.event.server.ClientPingServerEvent

class EventsCollection(private val bootstrap: UnifiedMetricsSpongeBootstrap) : CollectorCollection {

    private val loginCounter = Counter(Metrics.Events.Login, valueStoreFactory = VolatileDoubleStore)
    private val joinCounter = Counter(Metrics.Events.Join)
    private val quitCounter = Counter(Metrics.Events.Quit, valueStoreFactory = VolatileDoubleStore)
    private val chatCounter = Counter(Metrics.Events.Chat)
    private val pingCounter = Counter(Metrics.Events.Ping, valueStoreFactory = VolatileDoubleStore)

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
    fun onConnect(event: Auth) {
        loginCounter.inc()
    }

    @Listener
    fun onDisconnect(event: Disconnect) {
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