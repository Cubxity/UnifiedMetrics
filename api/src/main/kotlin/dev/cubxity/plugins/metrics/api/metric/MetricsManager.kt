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

package dev.cubxity.plugins.metrics.api.metric

import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.data.Metric

interface MetricsManager {
    val collections: List<CollectorCollection>

    fun initialize()

    fun registerCollection(collection: CollectorCollection)

    fun unregisterCollection(collection: CollectorCollection)

    fun registerDriver(name: String, factory: MetricsDriverFactory<out Any>)

    /**
     * This should be called asynchronously
     */
    suspend fun collect(): List<Metric>

    fun dispose()
}
