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

package dev.cubxity.plugins.metrics.sponge.metric

import dev.cubxity.plugins.metrics.api.metric.Metric
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.sponge.bootstrap.UnifiedMetricsSpongeBootstrap
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.message.MessageChannelEvent
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.event.server.ClientPingServerEvent

@Suppress("UNUSED_PARAMETER")
class EventsMetric(private val bootstrap: UnifiedMetricsSpongeBootstrap) : Metric {
    private val joinCounter = Counter("minecraft_events_join_total")
    private val quitCounter = Counter("minecraft_events_quit_total")
    private val chatCounter = Counter("minecraft_events_chat_total")
    private val pingCounter = Counter("minecraft_events_ping_total")

    override val collectors: List<MetricCollector> =
        listOf(joinCounter, quitCounter, chatCounter, pingCounter)

    override fun initialize() {
        Sponge.getEventManager().registerListeners(bootstrap.plugin, this)
    }

    override fun dispose() {
        Sponge.getEventManager().unregisterListeners(bootstrap.plugin)
    }

    @Listener
    fun onJoin(event: ClientConnectionEvent.Join) {
        joinCounter.inc()
    }

    @Listener
    fun onQuit(event: ClientConnectionEvent.Disconnect) {
        quitCounter.inc()
    }

    @Listener
    fun onChat(event: MessageChannelEvent.Chat) {
        chatCounter.inc()
    }

    @Listener
    fun onPing(event: ClientPingServerEvent) {
        pingCounter.inc()
    }
}