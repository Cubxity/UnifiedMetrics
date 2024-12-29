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

package dev.cubxity.plugins.metrics.velocity.bootstrap

import com.google.inject.Inject
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginDescription
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.cubxity.plugins.metrics.api.platform.PlatformType
import dev.cubxity.plugins.metrics.common.UnifiedMetricsBootstrap
import dev.cubxity.plugins.metrics.common.plugin.dispatcher.CurrentThreadDispatcher
import dev.cubxity.plugins.metrics.velocity.UnifiedMetricsVelocityPlugin
import dev.cubxity.plugins.metrics.velocity.logger.Slf4jLogger
import kotlinx.coroutines.CoroutineDispatcher
import java.nio.file.Path
import kotlin.jvm.optionals.getOrDefault

@Plugin(
    id = "unifiedmetrics",
    name = "UnifiedMetrics",
    description = "Fully-featured metrics plugin for Minecraft servers",
    authors = ["Cubxity"]
)
class UnifiedMetricsVelocityBootstrap @Inject constructor(
    @DataDirectory
    override val dataDirectory: Path,
    val server: ProxyServer,
    pluginLogger: org.slf4j.Logger,
    private val description: PluginDescription,
) : UnifiedMetricsBootstrap {
    private val plugin = UnifiedMetricsVelocityPlugin(this)

    override val type: PlatformType
        get() = PlatformType.Velocity

    override val version: String
        get() = description.version.getOrDefault("<unknown>")

    override val serverBrand: String
        get() = server.version.name

    override val configDirectory: Path
        get() = dataDirectory

    override val logger = Slf4jLogger(pluginLogger)

    override val dispatcher: CoroutineDispatcher = CurrentThreadDispatcher

    @Subscribe(order = PostOrder.FIRST)
    fun onEnable(event: ProxyInitializeEvent) {
        plugin.enable()
    }

    @Subscribe(order = PostOrder.LAST)
    fun onDisable(event: ProxyShutdownEvent) {
        plugin.disable()
    }
}