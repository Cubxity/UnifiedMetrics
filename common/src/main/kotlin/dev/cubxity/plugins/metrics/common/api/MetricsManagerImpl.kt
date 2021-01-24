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

import dev.cubxity.plugins.metrics.api.metric.Metric
import dev.cubxity.plugins.metrics.api.metric.MetricsManager
import dev.cubxity.plugins.metrics.common.metric.influx.InfluxMetricsDriver
import dev.cubxity.plugins.metrics.common.plugin.UnifiedMetricsPlugin
import java.util.*

class MetricsManagerImpl(private val plugin: UnifiedMetricsPlugin) : MetricsManager {
    private val driver = InfluxMetricsDriver(plugin)

    private var _metrics: MutableList<Metric<*>> = ArrayList()

    override val metrics: List<Metric<*>>
        get() = _metrics

    override fun initialize() {
        driver.connect()
        driver.scheduleTasks()
    }

    override fun registerMetric(metric: Metric<*>) {
        _metrics.add(metric)
    }

    override fun unregisterMetric(metric: Metric<*>) {
        _metrics.remove(metric)
        metric.dispose()
    }

    override fun writeMetrics(isSync: Boolean) {
        val points = metrics.filter { it.isSync == isSync }
            .flatMap { it.getMeasurements(plugin.apiProvider) }
            .map { it.serialize().tag("server", plugin.apiProvider.serverName) }

        driver.writePoints(points)
    }

    override fun dispose() {
        metrics.forEach { unregisterMetric(it) }
        driver.close()
    }
}