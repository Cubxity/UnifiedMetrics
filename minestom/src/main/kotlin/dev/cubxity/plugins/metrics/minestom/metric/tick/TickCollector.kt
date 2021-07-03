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

package dev.cubxity.plugins.metrics.minestom.metric.tick

import dev.cubxity.plugins.metrics.api.metric.collector.MILLISECONDS_PER_SECOND
import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.api.metric.data.GaugeSample
import dev.cubxity.plugins.metrics.api.metric.data.MetricSample
import net.minestom.server.MinecraftServer
import net.minestom.server.monitoring.TickMonitor
import java.util.function.Consumer

class TickCollector : Consumer<TickMonitor>, MetricCollector {
    private var lastTickTime: Long = 0
    private var ticks =  MinecraftServer.TICK_PER_SECOND

    private var lastTps = MinecraftServer.TICK_PER_SECOND
    private var lastTickDuration = 0.0

    override fun accept(monitor: TickMonitor) {
        lastTickDuration = monitor.tickTime / MILLISECONDS_PER_SECOND

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTickTime >= MILLISECONDS_PER_SECOND) {
            lastTps = ticks
            ticks = 1
        } else {
            ticks++
        }

        lastTickTime = currentTime
    }

    override fun collect(): List<MetricSample> = listOf(
        GaugeSample("minecraft_tps", lastTps),
        GaugeSample("minecraft_tick_duration_seconds", lastTickDuration),
    )
}