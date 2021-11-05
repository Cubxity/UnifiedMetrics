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

package dev.cubxity.plugins.metrics.bungee.metric.events.player

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.api.metric.store.VolatileDoubleStore
import dev.cubxity.plugins.metrics.bungee.bootstrap.UnifiedMetricsBungeeBootstrap
import dev.cubxity.plugins.metrics.common.metric.Metrics
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.PreLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

@Suppress("UNUSED_PARAMETER")
class EventsPlayerCollection(private val bootstrap: UnifiedMetricsBungeeBootstrap) : CollectorCollection, Listener {
    private val loginCounter = Counter(Metrics.Events.PlayerLogin)
    private val joinCounter = Counter(Metrics.Events.PlayerJoin, valueStoreFactory = VolatileDoubleStore)
    private val quitCounter = Counter(Metrics.Events.PlayerQuit, valueStoreFactory = VolatileDoubleStore)
    private val chatCounter = Counter(Metrics.Events.PlayerChat, valueStoreFactory = VolatileDoubleStore)

    override val collectors: List<Collector> = listOf(loginCounter, joinCounter, quitCounter, chatCounter)

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
}