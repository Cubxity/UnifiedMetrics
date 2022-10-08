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

package dev.cubxity.metrics.sponge.bootstrap

import com.google.inject.Inject
import dev.cubxity.metrics.sponge.SpongeDispatcher
import dev.cubxity.metrics.sponge.UnifiedMetricsSpongePlugin
import dev.cubxity.metrics.sponge.logger.Log4jLogger
import dev.cubxity.plugins.metrics.api.logging.Logger
import dev.cubxity.plugins.metrics.api.platform.PlatformType
import dev.cubxity.plugins.metrics.common.UnifiedMetricsBootstrap
import kotlinx.coroutines.CoroutineDispatcher
import org.spongepowered.api.Game
import org.spongepowered.api.MinecraftVersion
import org.spongepowered.api.Server
import org.spongepowered.api.config.ConfigManager
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.StartedEngineEvent
import org.spongepowered.api.plugin.PluginManager
import org.spongepowered.plugin.PluginContainer
import org.spongepowered.plugin.builtin.jvm.Plugin
import java.nio.file.Path

@Plugin("unifiedmetrics")
class UnifiedMetricsSpongeBootstrap @Inject constructor(
    serverLogger: org.apache.logging.log4j.Logger,
    //@ConfigDir(sharedRoot = false) override val dataDirectory: Path,
    val container: PluginContainer,
    serverVersion: MinecraftVersion,
    val pluginManager: PluginManager,
    private val game: Game,
    configManager: ConfigManager
): UnifiedMetricsBootstrap {

    val server: Server
        get() = game.server()

    private val plugin = UnifiedMetricsSpongePlugin(this)
    override val type: PlatformType = PlatformType.Sponge
    private val containerVersion = container.metadata().version()
    override val version: String = "${containerVersion.majorVersion}.${containerVersion.minorVersion}.${containerVersion.incrementalVersion}#${containerVersion.buildNumber}"
    override val serverBrand: String = serverVersion.name()
    override val configDirectory: Path = configManager.pluginConfig(container).configPath()
    override val dataDirectory: Path = configDirectory

    override val logger: Logger = Log4jLogger(serverLogger)
    override val dispatcher: CoroutineDispatcher = SpongeDispatcher(this)

    @Listener
    fun onServerStart(event: StartedEngineEvent<Server>) {
        plugin.enable()
    }
}