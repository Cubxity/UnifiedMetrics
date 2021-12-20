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

package dev.cubxity.plugins.metrics.krypton.metric.events

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.api.metric.store.VolatileDoubleStore
import dev.cubxity.plugins.metrics.common.metric.Metrics
import dev.cubxity.plugins.metrics.krypton.bootstrap.UnifiedMetricsKryptonBootstrap
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.player.ChatEvent
import org.kryptonmc.api.event.player.JoinEvent
import org.kryptonmc.api.event.player.LoginEvent
import org.kryptonmc.api.event.player.QuitEvent

class EventsCollection(private val bootstrap: UnifiedMetricsKryptonBootstrap) : CollectorCollection {
    private val loginCounter = Counter(Metrics.Events.Login)
    private val joinCounter = Counter(Metrics.Events.Join, valueStoreFactory = VolatileDoubleStore)
    private val quitCounter = Counter(Metrics.Events.Quit, valueStoreFactory = VolatileDoubleStore)
    private val chatCounter = Counter(Metrics.Events.Chat)

    override val collectors: List<Collector> = listOf(loginCounter, joinCounter, quitCounter, chatCounter)

    override val isAsync: Boolean
        get() = true

    override fun initialize() {
        bootstrap.server.eventManager.register(bootstrap, this)
    }

    override fun dispose() {
        bootstrap.server.eventManager.unregisterListener(bootstrap, this)
    }

    @Listener
    fun onLogin(event: LoginEvent) {
        loginCounter.inc()
    }

    @Listener
    fun onJoin(event: JoinEvent) {
        joinCounter.inc()
    }

    @Listener
    fun onQuit(event: QuitEvent) {
        quitCounter.inc()
    }

    @Listener
    fun onChat(event: ChatEvent) {
        chatCounter.inc()
    }
}
