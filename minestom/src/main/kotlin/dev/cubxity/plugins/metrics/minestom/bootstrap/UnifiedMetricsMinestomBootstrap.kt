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

package dev.cubxity.plugins.metrics.minestom.bootstrap

import dev.cubxity.plugins.metrics.api.logging.Logger
import dev.cubxity.plugins.metrics.api.platform.PlatformType
import dev.cubxity.plugins.metrics.common.UnifiedMetricsBootstrap
import dev.cubxity.plugins.metrics.common.plugin.dispatcher.CurrentThreadDispatcher
import dev.cubxity.plugins.metrics.minestom.UnifiedMetricsMinestomPlugin
import dev.cubxity.plugins.metrics.minestom.logger.Slf4jLogger
import kotlinx.coroutines.CoroutineDispatcher
import net.minestom.server.MinecraftServer
import java.nio.file.Path

class UnifiedMetricsMinestomBootstrap(private val extension: UnifiedMetricsMinestomExtension) : UnifiedMetricsBootstrap {
    private val plugin = UnifiedMetricsMinestomPlugin(this)

    override val type: PlatformType
        get() = PlatformType.Minestom

    override val version: String
        get() = "1.0" // I don't think Minestom has a versioning system yet

    override val serverBrand: String
        get() = MinecraftServer.getBrandName()

    override val dataDirectory: Path
       get() = extension.dataDirectory

    override val configDirectory: Path
        get() = extension.dataDirectory

    override val logger: Logger = Slf4jLogger(extension.logger)

    override val dispatcher: CoroutineDispatcher = CurrentThreadDispatcher

    fun initialize() {
        plugin.enable()
    }

    fun terminate() {
        plugin.disable()
    }
}