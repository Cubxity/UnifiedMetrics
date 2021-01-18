/*
 *     UnifiedMetrics is a fully-featured metrics collection plugin for Spigot.
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

package dev.cubxity.plugins.metrics.metric

import dev.cubxity.plugins.metrics.UnifiedMetrics
import dev.cubxity.plugins.metrics.measurement.EventsMeasurement
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.concurrent.atomic.AtomicLong

class EventsMetric : Metric<EventsMeasurement>, Listener {
    private var joinCount: Long = 0
    private var quitCount: Long = 0
    private val chatCount = AtomicLong()

    override val isSync: Boolean
        get() = true

    override fun initialize(plugin: UnifiedMetrics) {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    override fun getMeasurements(plugin: UnifiedMetrics): List<EventsMeasurement> {
        val server = plugin.metricsConfig.influx.server
        val joinCount = joinCount
        val quitCount = quitCount
        val chatCount = chatCount.getAndSet(0)

        this.joinCount = 0
        this.quitCount = 0

        return listOf(EventsMeasurement(server, joinCount, quitCount, chatCount))
    }

    override fun dispose(plugin: UnifiedMetrics) {
        HandlerList.unregisterAll(this)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        joinCount++
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        quitCount++
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        chatCount.addAndGet(1)
    }
}