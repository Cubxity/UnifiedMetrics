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

package dev.cubxity.plugins.metrics.forge.bootstrap

import dev.cubxity.plugins.metrics.api.platform.PlatformType
import dev.cubxity.plugins.metrics.common.UnifiedMetricsBootstrap
import dev.cubxity.plugins.metrics.common.plugin.dispatcher.CurrentThreadDispatcher
import dev.cubxity.plugins.metrics.forge.UnifiedMetricsForgePlugin
import dev.cubxity.plugins.metrics.forge.logger.Log4jLogger
import kotlinx.coroutines.CoroutineDispatcher
import net.minecraft.server.MinecraftServer
import net.minecraftforge.event.server.ServerStartedEvent
import net.minecraftforge.event.server.ServerStoppingEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.minecraftforge.fml.loading.FMLPaths
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist
import java.nio.file.Path

private const val pluginVersion = "@version@"

@Mod("unifiedmetrics")
class UnifiedMetricsForgeBootstrap : UnifiedMetricsBootstrap {
    private val plugin = UnifiedMetricsForgePlugin(this)
    lateinit var server: MinecraftServer

    override val type: PlatformType
        get() = PlatformType.Forge

    override val version: String
        get() = pluginVersion

    override val serverBrand: String
        get() = server.serverModName

    override val dataDirectory: Path = FMLPaths.CONFIGDIR.get().resolve("unifiedmetrics")

    override val configDirectory: Path = FMLPaths.CONFIGDIR.get().resolve("unifiedmetrics")

    override val logger = Log4jLogger(LogManager.getLogger("UnifiedMetrics"))

    override val dispatcher: CoroutineDispatcher = CurrentThreadDispatcher

    init {
        MOD_BUS.addListener(::onServerSetup)
    }

    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        FORGE_BUS.addListener(::onServerStarted)
        FORGE_BUS.addListener(::onServerStopping)
    }

    private fun onServerStarted(event: ServerStartedEvent) {
        this.server = event.server
        plugin.enable()
    }


    private fun onServerStopping(event: ServerStoppingEvent) {
        plugin.disable()
    }
}