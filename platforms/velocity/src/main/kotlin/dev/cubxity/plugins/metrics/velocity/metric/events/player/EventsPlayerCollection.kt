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

package dev.cubxity.plugins.metrics.velocity.metric.events.player

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.PreLoginEvent
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent
import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.common.metric.Metrics
import dev.cubxity.plugins.metrics.velocity.bootstrap.UnifiedMetricsVelocityBootstrap

@Suppress("UNUSED_PARAMETER")
class EventsPlayerCollection(private val bootstrap: UnifiedMetricsVelocityBootstrap) : CollectorCollection {
    private val loginCounter = Counter(Metrics.Events.PlayerLogin)
    private val joinCounter = Counter(Metrics.Events.PlayerJoin)
    private val quitCounter = Counter(Metrics.Events.PlayerQuit)
    private val chatCounter = Counter(Metrics.Events.PlayerChat)

    override val collectors: List<Collector> = listOf(loginCounter, joinCounter, quitCounter, chatCounter)

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
}