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

@file:JvmName("UnifiedMetricsMinestomServer")

package dev.cubxity.plugins.metrics.minestom

import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.world.DimensionType

fun main() {
    val server = MinecraftServer.init()

    val instanceManager = MinecraftServer.getInstanceManager()
    val instance = instanceManager.createInstanceContainer(DimensionType.OVERWORLD)
    instanceManager.registerInstance(instance)

    val listener = EventListener.of(PlayerLoginEvent::class.java) {
        it.setSpawningInstance(instance)
    }
    MinecraftServer.getGlobalEventHandler().addListener(listener)

    server.start("127.0.0.1", 25565)
}