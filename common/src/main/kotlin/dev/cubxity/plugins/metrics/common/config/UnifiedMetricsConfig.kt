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

package dev.cubxity.plugins.metrics.common.config

import kotlinx.serialization.Serializable

@Serializable
data class UnifiedMetricsConfig(
    val server: UnifiedMetricsServerConfig = UnifiedMetricsServerConfig(),
    val metrics: UnifiedMetricsMetricsConfig = UnifiedMetricsMetricsConfig()
)

@Serializable
data class UnifiedMetricsServerConfig(
    val name: String = env("SERVER_NAME", "global")
)

@Serializable
data class UnifiedMetricsMetricsConfig(
    val enabled: Boolean = true,
    val driver: String = "prometheus",
    val collectors: UnifiedMetricsCollectorsConfig = UnifiedMetricsCollectorsConfig()
)

@Serializable
data class UnifiedMetricsCollectorsConfig(
    val systemGc: Boolean = true,
    val systemMemory: Boolean = true,
    val systemProcess: Boolean = true,
    val systemThread: Boolean = true,
    val server: Boolean = true,
    val world: Boolean = true,
    val tick: Boolean = true,
    val events: Boolean = true,
    val regionizedServer: Boolean = true
)

private fun env(name: String, default: String): String =
    System.getenv("UNIFIEDMETRICS_$name") ?: default
