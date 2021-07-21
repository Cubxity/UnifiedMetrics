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

package dev.cubxity.plugins.metrics.api

import dev.cubxity.plugins.metrics.api.logging.Logger
import dev.cubxity.plugins.metrics.api.metric.MetricsManager
import dev.cubxity.plugins.metrics.api.platform.Platform
import kotlinx.coroutines.CoroutineDispatcher

/**
 * The UnifiedMetrics API
 */
interface UnifiedMetrics {
    /**
     * The platform UnifiedMetrics is running on.
     */
    val platform: Platform

    /**
     * The name of this server.
     *
     * This is defined in the UnifiedMetrics configuration file, and is used for
     * grouping server data.
     *
     * The default server name is "main"
     */
    val serverName: String

    /**
     * The platform's logger.
     */
    val logger: Logger

    /**
     * The platform's dispatcher.
     */
    val dispatcher: CoroutineDispatcher

    /**
     * The metrics api.
     */
    val metricsManager: MetricsManager
}