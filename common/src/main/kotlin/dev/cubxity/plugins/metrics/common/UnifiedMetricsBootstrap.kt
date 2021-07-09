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

package dev.cubxity.plugins.metrics.common

import dev.cubxity.plugins.metrics.api.logging.Logger
import dev.cubxity.plugins.metrics.api.platform.PlatformType
import kotlinx.coroutines.CoroutineDispatcher
import java.nio.file.Path

interface UnifiedMetricsBootstrap {
    /**
     * The plugin's platform type
     */
    val type: PlatformType

    /**
     * The installed plugin's version
     */
    val version: String

    /**
     * The server's brand
     */
    val serverBrand: String

    /**
     * The plugin's data directory
     */
    val dataDirectory: Path

    /**
     * The plugin's config directory
     */
    val configDirectory: Path

    /**
     * The platform's logger
     */
    val logger: Logger

    /**
     * The platform's dispatcher
     */
    val dispatcher: CoroutineDispatcher
}