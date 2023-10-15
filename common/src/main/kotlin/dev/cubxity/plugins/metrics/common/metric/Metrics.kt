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

package dev.cubxity.plugins.metrics.common.metric

object Metrics {
    object Events {
        const val Login = "minecraft_events_login_total"
        const val Join = "minecraft_events_join_total"
        const val Quit = "minecraft_events_quit_total"
        const val Chat = "minecraft_events_chat_total"
        const val Ping = "minecraft_events_ping_total"
    }

    object Server {
        const val Plugins = "minecraft_plugins"
        const val PlayersCount = "minecraft_players_count"
        const val PlayersMax = "minecraft_players_max"
        const val TickDurationSeconds = "minecraft_tick_duration_seconds"
        const val WorldEntitiesCount = "minecraft_world_entities_count"
        const val WorldPlayersCount = "minecraft_world_players_count"
        const val WorldLoadedChunks = "minecraft_world_loaded_chunks"
    }

    object RegionizedServer {
        const val RegionCount = "minecraft_regionized_region_count"
        const val RegionTick = "minecraft_regionized_region_tick_total"
        const val RegionEntitiesCount = "minecraft_regionized_region_entities_count"
        const val RegionPlayersCount = "minecraft_regionized_region_players_count"
        const val RegionChunksCount = "minecraft_regionized_region_chunks_count"
    }
}