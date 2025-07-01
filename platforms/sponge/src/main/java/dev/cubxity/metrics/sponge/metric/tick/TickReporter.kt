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

package dev.cubxity.metrics.sponge.metric.tick

import dev.cubxity.metrics.sponge.bootstrap.UnifiedMetricsSpongeBootstrap
import org.spongepowered.api.Sponge
import org.spongepowered.api.scheduler.ScheduledTask
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.util.Ticks

class TickReporter(
    private val metric: TickCollection,
    bootstrap: UnifiedMetricsSpongeBootstrap
) {

    private lateinit var task: ScheduledTask

    private val taskExecutor = Task.builder()
        .delay(Ticks.of(1))
        .execute(Runnable {
            metric.onTick(0.0)
            this.startTask()
        })
        .plugin(bootstrap.container)

    fun initialize() {
        startTask()
    }

    private fun startTask() {
        task = Sponge.asyncScheduler().submit(taskExecutor.build())
    }

    fun dispose() {
        task.cancel()
    }

}