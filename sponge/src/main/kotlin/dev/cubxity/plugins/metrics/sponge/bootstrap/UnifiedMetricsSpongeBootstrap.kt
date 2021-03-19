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

package dev.cubxity.plugins.metrics.sponge.bootstrap

import dev.cubxity.plugins.metrics.api.platform.PlatformType
import dev.cubxity.plugins.metrics.common.UnifiedMetricsBootstrap
import dev.cubxity.plugins.metrics.sponge.SpongeSchedulerAdapter
import dev.cubxity.plugins.metrics.sponge.logger.Slf4jLogger
import org.slf4j.Logger
import java.nio.file.Path

// TODO: Automatically replace this at build
private const val pluginVersion = "0.2.1"

@Suppress("MemberVisibilityCanBePrivate")
class UnifiedMetricsSpongeBootstrap(
    var plugin: UMSpongePlugin
) : UnifiedMetricsBootstrap {

    override val type: PlatformType
        get() = PlatformType.Sponge

    override val version: String
        get() = pluginVersion

    override val serverBrand: String
        get() = "sponge-powered"

    override val dataDirectory: Path
        get() = plugin.configDir!!

    override val configDirectory: Path
        get() = plugin.configDir!!

    override val logger = Slf4jLogger(plugin.internalLogger!!)

    override val scheduler = SpongeSchedulerAdapter(this)
}