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

package dev.cubxity.plugins.metrics.fabric.bootstrap

import dev.cubxity.plugins.metrics.api.platform.PlatformType
import dev.cubxity.plugins.metrics.common.UnifiedMetricsBootstrap
import dev.cubxity.plugins.metrics.common.plugin.dispatcher.CurrentThreadDispatcher
import dev.cubxity.plugins.metrics.fabric.UnifiedMetricsFabricPlugin
import dev.cubxity.plugins.metrics.fabric.logger.Log4jLogger
import kotlinx.coroutines.CoroutineDispatcher
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import org.apache.logging.log4j.LogManager
import java.nio.file.Path

private const val pluginVersion = "@version@"

class UnifiedMetricsFabricBootstrap : DedicatedServerModInitializer, UnifiedMetricsBootstrap {
    private val plugin = UnifiedMetricsFabricPlugin(this)
    lateinit var server: MinecraftServer

    override val type: PlatformType
        get() = PlatformType.Fabric

    override val version: String
        get() = pluginVersion

    override val serverBrand: String
        get() = server.serverModName

    override val dataDirectory: Path
        = FabricLoader.getInstance().configDir.resolve("unifiedmetrics")

    override val configDirectory: Path
        = FabricLoader.getInstance().configDir.resolve("unifiedmetrics")

    override val logger = Log4jLogger(LogManager.getLogger("UnifiedMetrics"))

    override val dispatcher: CoroutineDispatcher = CurrentThreadDispatcher

    override fun onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register {
            server = it
            plugin.enable()
        }

        ServerLifecycleEvents.SERVER_STOPPING.register {
            plugin.disable()
        }
    }
}