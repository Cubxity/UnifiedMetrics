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

package dev.cubxity.plugins.metrics.bukkit.metric

import dev.cubxity.plugins.metrics.api.metric.Metric
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.bukkit.bootstrap.UnifiedMetricsBukkitBootstrap
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.ServerListPingEvent

@Suppress("UNUSED_PARAMETER")
class EventsMetric(private val bootstrap: UnifiedMetricsBukkitBootstrap) : Metric, Listener {
    private val joinCounter = Counter("minecraft_events_join_total")
    private val quitCounter = Counter("minecraft_events_quit_total")
    private val chatCounter = Counter("minecraft_events_chat_total")
    private val pingCounter = Counter("minecraft_events_ping_total")

    override val collectors: List<MetricCollector> =
        listOf(joinCounter, quitCounter, chatCounter, pingCounter)

    override fun initialize() {
        Bukkit.getPluginManager().registerEvents(this, bootstrap)
    }

    override fun dispose() {
        HandlerList.unregisterAll(this)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        joinCounter.inc()
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        quitCounter.inc()
    }

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        chatCounter.inc()
    }

    @EventHandler
    fun onPing(event: ServerListPingEvent) {
        pingCounter.inc()
    }
}