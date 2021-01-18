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
import dev.cubxity.plugins.metrics.measurement.WorldMeasurement
import org.bukkit.Bukkit
import java.util.*

class WorldMetric : Metric<WorldMeasurement> {
    override val isSync: Boolean
        get() = true

    override fun getMeasurements(plugin: UnifiedMetrics): List<WorldMeasurement> {
        val worlds = Bukkit.getWorlds()
        val measurements: MutableList<WorldMeasurement> = ArrayList(worlds.size)
        val server = plugin.metricsConfig.influx.server

        for (world in worlds) {
            measurements.add(
                WorldMeasurement(
                    server, world.name,
                    world.entities.size, world.loadedChunks.size
                )
            )
        }
        return measurements
    }
}