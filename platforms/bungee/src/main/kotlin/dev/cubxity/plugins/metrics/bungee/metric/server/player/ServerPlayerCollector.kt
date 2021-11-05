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

package dev.cubxity.plugins.metrics.bungee.metric.server.player

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.bungee.bootstrap.UnifiedMetricsBungeeBootstrap
import dev.cubxity.plugins.metrics.common.metric.Metrics

class ServerPlayerCollector(private val bootstrap: UnifiedMetricsBungeeBootstrap) : Collector {
    override fun collect(): List<Metric> {
        val proxy = bootstrap.proxy
        return listOf(
            GaugeMetric(Metrics.Server.PlayersCount, value = proxy.onlineCount),
            GaugeMetric(Metrics.Server.PlayersMax, value = proxy.config.playerLimit)
        )
    }
}