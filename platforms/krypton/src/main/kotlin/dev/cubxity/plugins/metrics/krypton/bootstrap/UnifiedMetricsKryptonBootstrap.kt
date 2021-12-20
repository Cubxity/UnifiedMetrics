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

package dev.cubxity.plugins.metrics.krypton.bootstrap

import com.google.inject.Inject
import dev.cubxity.plugins.metrics.api.platform.PlatformType
import dev.cubxity.plugins.metrics.common.UnifiedMetricsBootstrap
import dev.cubxity.plugins.metrics.common.plugin.dispatcher.CurrentThreadDispatcher
import dev.cubxity.plugins.metrics.krypton.UnifiedMetricsKryptonPlugin
import dev.cubxity.plugins.metrics.krypton.logger.Log4JLogger
import kotlinx.coroutines.CoroutineDispatcher
import org.kryptonmc.api.Server
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.ListenerPriority
import org.kryptonmc.api.event.server.ServerStartEvent
import org.kryptonmc.api.event.server.ServerStopEvent
import org.kryptonmc.api.plugin.annotation.DataFolder
import org.kryptonmc.api.plugin.annotation.Plugin
import java.nio.file.Path

private const val pluginVersion = "@version@"

@Plugin(
    "unifiedmetrics",
    "UnifiedMetrics",
    pluginVersion,
    "Fully-featured metrics plugin for Minecraft servers",
    ["Cubxity"]
)
class UnifiedMetricsKryptonBootstrap @Inject constructor(
    @DataFolder
    override val dataDirectory: Path,
    val server: Server,
    pluginLogger: org.apache.logging.log4j.Logger
) : UnifiedMetricsBootstrap {
    private val plugin = UnifiedMetricsKryptonPlugin(this)

    override val type: PlatformType
        get() = PlatformType.Krypton

    override val version: String
        get() = pluginVersion

    override val serverBrand: String
        get() = server.platform.name

    override val configDirectory: Path
        get() = dataDirectory

    override val logger = Log4JLogger(pluginLogger)

    override val dispatcher: CoroutineDispatcher = CurrentThreadDispatcher

    @Listener(ListenerPriority.MAXIMUM)
    fun onStart(event: ServerStartEvent) {
        plugin.enable()
    }

    @Listener(ListenerPriority.NONE)
    fun onStop(event: ServerStopEvent) {
        plugin.disable()
    }
}
