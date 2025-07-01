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

package dev.cubxity.metrics.sponge

import dev.cubxity.metrics.sponge.bootstrap.UnifiedMetricsSpongeBootstrap
import kotlinx.coroutines.*
import org.spongepowered.api.scheduler.Task
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

@OptIn(InternalCoroutinesApi::class)
class SpongeDispatcher(private val plugin: UnifiedMetricsSpongeBootstrap): CoroutineDispatcher(), Delay {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if(!context.isActive) return
        if(plugin.server.onMainThread()) {
            block.run()
        }else {
            plugin.server.scheduler().submit(Task.builder().execute(block).plugin(plugin.container).build())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val task = plugin.server.scheduler().submit(
            Task.builder()
                .delay(timeMillis, TimeUnit.MILLISECONDS)
                .execute(Runnable {
                    continuation.apply { resumeUndispatched(Unit) }
                })
                .plugin(plugin.container)
                .build()
        )
        continuation.invokeOnCancellation { task.cancel() }
    }
}