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
import dev.cubxity.plugins.metrics.measurement.TPSMeasurement
import org.bukkit.Bukkit

class TPSMetric : Metric<TPSMeasurement>, Runnable {
    private var taskId = -1
    private var currentSec: Long = 0
    private var ticks = 0
    private var tps = 20.0

    override val isSync: Boolean
        get() = false

    override fun initialize(plugin: UnifiedMetrics) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 1)
    }

    override fun getMeasurements(plugin: UnifiedMetrics): List<TPSMeasurement> {
        return listOf(TPSMeasurement(plugin.metricsConfig.influx.server, tps))
    }

    override fun dispose(plugin: UnifiedMetrics) {
        Bukkit.getScheduler().cancelTask(taskId)
    }

    override fun run() {
        val sec = System.currentTimeMillis() / 1000
        if (currentSec == sec) {
            ticks++
        } else {
            currentSec = sec
            tps = if (tps == 0.0) ticks.toDouble() else (tps + ticks) / 2
            ticks = 0
        }
    }
}