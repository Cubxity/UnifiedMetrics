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

package dev.cubxity.plugins.metrics.common.plugin.scheduler

import dev.cubxity.plugins.metrics.api.scheduler.SchedulerAdapter
import dev.cubxity.plugins.metrics.api.scheduler.SchedulerTask
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

abstract class AbstractJavaScheduler : SchedulerAdapter {
    private val executor = Executors.newScheduledThreadPool(1) {
        Thread(it, "unifiedmetrics-scheduler").apply { isDaemon = true }
    }

    override fun asyncRepeating(task: Runnable, interval: Long, unit: TimeUnit): SchedulerTask {
        val future = executor.scheduleAtFixedRate(task, interval, interval, unit)
        return SchedulerTask { future.cancel(false) }
    }

    override fun shutdown() {
        executor.shutdown()
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS)
        } catch (exception: InterruptedException) {
            exception.printStackTrace()
        }
    }
}