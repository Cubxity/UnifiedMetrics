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

package dev.cubxity.plugins.metrics.common.measurement

import dev.cubxity.plugins.metrics.api.metric.data.Measurement
import dev.cubxity.plugins.metrics.api.metric.data.Point

data class ServerMeasurement(
    val plugins: Int,
    val players: Int,
    val maxPlayers: Int
): Measurement {
    override fun serialize() = Point("server")
        .field("plugins", plugins)
        .field("players", players)
        .field("max_players", maxPlayers)
}