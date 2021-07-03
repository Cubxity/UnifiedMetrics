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

package dev.cubxity.plugins.metrics.minestom.metric.world

import dev.cubxity.plugins.metrics.api.metric.collector.MetricCollector
import dev.cubxity.plugins.metrics.api.metric.data.GaugeSample
import dev.cubxity.plugins.metrics.api.metric.data.MetricSample
import net.minestom.server.MinecraftServer

class WorldCollector : MetricCollector {
    override fun collect(): List<MetricSample> {
        val instances = MinecraftServer.getInstanceManager().instances
        val samples = ArrayList<MetricSample>(instances.size * 3)

        for (instance in instances) {
            val tags = mapOf("name" to instance.uniqueId.toString())
            samples.add(GaugeSample("minecraft_world_entities_count", instance.entities.size, tags))
            samples.add(GaugeSample("minecraft_world_players_count", instance.players.size, tags))
            samples.add(GaugeSample("minecraft_world_loaded_chunks", instance.chunks.size, tags))
        }

        return samples
    }
}