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

package dev.cubxity.plugins.metrics.bukkit.metric.world

import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.api.metric.data.GaugeSample
import dev.cubxity.plugins.metrics.api.metric.data.MetricSample
import org.bukkit.Bukkit

class WorldCollector : MetricCollector {
    override fun collect(): List<MetricSample> {
        val worlds = Bukkit.getWorlds().toList()
        val samples = ArrayList<MetricSample>(worlds.size * 3)

        for (world in worlds) {
            val tags = mapOf("name" to world.name)
            samples.add(GaugeSample("minecraft_world_entities_count", world.entities.size, tags))
            samples.add(GaugeSample("minecraft_world_players_count", world.players.size, tags))
            samples.add(GaugeSample("minecraft_world_loaded_chunks", world.loadedChunks.size, tags))
        }

        return samples
    }
}