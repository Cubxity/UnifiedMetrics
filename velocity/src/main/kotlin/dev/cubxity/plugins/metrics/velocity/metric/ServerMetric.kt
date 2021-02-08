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

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.api.metric.Metric
import dev.cubxity.plugins.metrics.common.measurement.ServerMeasurement
import dev.cubxity.plugins.metrics.velocity.bootstrap.UnifiedMetricsVelocityBootstrap

class ServerMetric(private val bootstrap: UnifiedMetricsVelocityBootstrap) : Metric<ServerMeasurement> {
    override val isSync: Boolean
        get() = false

    override fun getMeasurements(api: UnifiedMetrics): List<ServerMeasurement> {
        val server = bootstrap.server
        val plugins = server.pluginManager.plugins.size
        val playerCount = server.playerCount
        val maxPlayers = server.configuration.showMaxPlayers

        return listOf(ServerMeasurement(plugins, playerCount, maxPlayers))
    }
}