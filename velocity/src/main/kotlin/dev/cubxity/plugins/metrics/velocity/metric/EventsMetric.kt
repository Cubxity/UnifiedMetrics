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

package dev.cubxity.plugins.metrics.velocity.metric

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.player.PlayerChatEvent
import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.api.metric.Metric
import dev.cubxity.plugins.metrics.common.measurement.EventsMeasurement
import dev.cubxity.plugins.metrics.velocity.bootstrap.UnifiedMetricsVelocityBootstrap
import java.util.concurrent.atomic.AtomicLong

class EventsMetric(private val bootstrap: UnifiedMetricsVelocityBootstrap) : Metric<EventsMeasurement> {
    private val joinCount = AtomicLong()
    private val quitCount = AtomicLong()
    private val chatCount = AtomicLong()

    override val isSync: Boolean
        get() = false

    override fun initialize() {
        bootstrap.server.eventManager.register(bootstrap, this)
    }

    override fun getMeasurements(api: UnifiedMetrics): List<EventsMeasurement> {
        val joinCount = joinCount.getAndSet(0)
        val quitCount = quitCount.getAndSet(0)
        val chatCount = chatCount.getAndSet(0)

        return listOf(EventsMeasurement(joinCount, quitCount, chatCount))
    }

    override fun dispose() {
        bootstrap.server.eventManager.unregisterListener(bootstrap, this)
    }

    @Subscribe
    fun onConnect(event: LoginEvent) {
        joinCount.incrementAndGet()
    }

    @Subscribe
    fun onDisconnect(event: DisconnectEvent) {
        quitCount.incrementAndGet()
    }

    @Subscribe
    fun onChat(event: PlayerChatEvent) {
        chatCount.incrementAndGet()
    }
}