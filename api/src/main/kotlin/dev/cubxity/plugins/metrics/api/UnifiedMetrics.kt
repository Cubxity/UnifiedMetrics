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

package dev.cubxity.plugins.metrics.api

import dev.cubxity.plugins.metrics.api.logging.Logger
import dev.cubxity.plugins.metrics.api.metric.MetricsManager
import dev.cubxity.plugins.metrics.api.platform.Platform
import dev.cubxity.plugins.metrics.api.scheduler.SchedulerAdapter

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
     * The platform's scheduler.
     */
    val scheduler: SchedulerAdapter

    /**
     * The metrics api.
     */
    val metricsManager: MetricsManager
}