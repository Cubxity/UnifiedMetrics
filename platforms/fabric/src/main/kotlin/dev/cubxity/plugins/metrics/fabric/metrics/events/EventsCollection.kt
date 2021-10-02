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

package dev.cubxity.plugins.metrics.fabric.metrics.events

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.api.metric.store.VolatileDoubleStore
import dev.cubxity.plugins.metrics.common.metric.Metrics
import dev.cubxity.plugins.metrics.fabric.events.ChatEvent
import dev.cubxity.plugins.metrics.fabric.events.PingEvent
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

class EventsCollection : CollectorCollection {
    private val loginCounter = Counter(Metrics.Events.Login)
    private val joinCounter = Counter(Metrics.Events.Join)
    private val quitCounter = Counter(Metrics.Events.Quit)
    private val chatCounter = Counter(Metrics.Events.Chat, valueStoreFactory = VolatileDoubleStore)
    private val pingCounter = Counter(Metrics.Events.Ping)

    override val collectors: List<Collector> =
        listOf(loginCounter, joinCounter, quitCounter, chatCounter, pingCounter)

    override fun initialize() {
        ServerLoginConnectionEvents.INIT.register { _, _ ->
            loginCounter.inc()
        }
        ServerPlayConnectionEvents.JOIN.register { _, _, _ ->
            joinCounter.inc()
        }
        ServerPlayConnectionEvents.DISCONNECT.register { _, _ ->
            quitCounter.inc()
        }
        ChatEvent.event.register {
            chatCounter.inc()
        }
        PingEvent.event.register {
            pingCounter.inc()
        }
    }
}