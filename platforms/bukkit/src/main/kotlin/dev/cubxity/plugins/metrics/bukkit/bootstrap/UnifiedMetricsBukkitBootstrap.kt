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

package dev.cubxity.plugins.metrics.bukkit.bootstrap

import dev.cubxity.plugins.metrics.api.platform.PlatformType
import dev.cubxity.plugins.metrics.bukkit.BukkitDispatcher
import dev.cubxity.plugins.metrics.bukkit.FoliaDispatcher
import dev.cubxity.plugins.metrics.bukkit.UnifiedMetricsBukkitPlugin
import dev.cubxity.plugins.metrics.bukkit.util.BukkitPlatform
import dev.cubxity.plugins.metrics.common.UnifiedMetricsBootstrap
import dev.cubxity.plugins.metrics.common.plugin.logger.JavaLogger
import kotlinx.coroutines.CoroutineDispatcher
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Path

@Suppress("MemberVisibilityCanBePrivate")
class UnifiedMetricsBukkitBootstrap : JavaPlugin(), UnifiedMetricsBootstrap {
    private val plugin = UnifiedMetricsBukkitPlugin(this)

    override val type: PlatformType
        get() = PlatformType.Bukkit

    override val version: String
        get() = description.version

    override val serverBrand: String
        get() = server.name

    override val dataDirectory: Path
        get() = dataFolder.toPath()

    override val configDirectory: Path
        get() = dataFolder.toPath()

    override val logger = JavaLogger(getLogger())

    override val dispatcher: CoroutineDispatcher = when (BukkitPlatform.current) {
        BukkitPlatform.Folia -> FoliaDispatcher()
        else -> BukkitDispatcher(this)
    }

    override fun onEnable() {
        (this as UnifiedMetricsBootstrap).logger.info("Running on Bukkit platform ${BukkitPlatform.current}")
        plugin.enable()
    }

    override fun onDisable() {
        plugin.disable()
    }
}