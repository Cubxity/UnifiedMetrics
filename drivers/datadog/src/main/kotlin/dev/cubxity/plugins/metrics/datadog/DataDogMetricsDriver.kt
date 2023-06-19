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

package dev.cubxity.plugins.metrics.datadog

import dev.cubxity.plugins.metrics.api.UnifiedMetrics
import dev.cubxity.plugins.metrics.api.metric.MetricsDriver
import dev.cubxity.plugins.metrics.api.metric.data.CounterMetric
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric
import dev.cubxity.plugins.metrics.api.metric.data.HistogramMetric
import dev.cubxity.plugins.metrics.api.metric.data.Metric
import dev.cubxity.plugins.metrics.api.util.fastForEach
import kotlinx.coroutines.*
import kotlin.math.max
import kotlin.system.measureTimeMillis
import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;
import java.util.*

class DataDogMetricsDriver(private val api: UnifiedMetrics, private val config: DataDogConfig) : MetricsDriver {
    private val coroutineScope = CoroutineScope(Dispatchers.Default) + SupervisorJob()

    private var statsdClient: StatsDClient? = null

    override fun initialize() {
        statsdClient = NonBlockingStatsDClientBuilder().prefix("statsd").build()
    }

    override fun close() {
        coroutineScope.cancel()
        statsdClient?.close()
        statsdClient = null
    }

    private fun scheduleTasks() {
        val interval = 1000

        coroutineScope.launch {
            while (true) {
                val time = measureTimeMillis {
                    try {
                        val metrics = api.metricsManager.collect()
                        writeMetrics(metrics)
                    } catch (error: Throwable) {
                        api.logger.severe("An error occurred whilst writing samples to DataDog", error)
                    }
                }
                delay(max(0, interval - time))
            }
        }
    }

    private fun writeMetrics(metrics: List<Metric>) {
        metrics.fastForEach { metric ->
            val intMutableList: MutableList<String> = mutableListOf<String>()
            for (entry in metric.labels.entries) {
                intMutableList.add(entry.key);
                intMutableList.add(entry.value);
            }
            when (metric) {
                is GaugeMetric -> statsdClient?.gauge(metric.name, metric.value, *intMutableList.toTypedArray())
                is CounterMetric -> statsdClient?.count(metric.name, metric.value, *intMutableList.toTypedArray())
                is HistogramMetric -> {
                    metric.bucket.fastForEach { bucket ->
                        var temp = bucket.cumulativeCount
                        while (temp-- > 0) {
                            statsdClient?.histogram(metric.name, bucket.upperBound, *intMutableList.toTypedArray())
                        }
                    }
                }
            }
        }
    }
}