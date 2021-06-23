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

package dev.cubxity.plugins.metrics.common.api

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.api.logging.Logger
import dev.cubxity.plugins.metrics.api.metric.MetricsManager
import dev.cubxity.plugins.metrics.api.platform.Platform
import dev.cubxity.plugins.metrics.common.config.ServerSpec
import dev.cubxity.plugins.metrics.common.plugin.UnifiedMetricsPlugin
import kotlinx.coroutines.CoroutineDispatcher

open class UnifiedMetricsApiProvider(val plugin: UnifiedMetricsPlugin) : UnifiedMetrics {
    override val platform: Platform = PlatformImpl(plugin)

    override val serverName: String
        get() = plugin.config[ServerSpec.name]

    override val logger: Logger
        get() = plugin.bootstrap.logger

    override val dispatcher: CoroutineDispatcher
        get() = plugin.bootstrap.dispatcher

    override val metricsManager: MetricsManager =
        MetricsManagerImpl(plugin)
}