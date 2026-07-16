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

package dev.cubxity.plugins.metrics.bukkit

import kotlinx.coroutines.*
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Consumer
import kotlin.coroutines.CoroutineContext
import kotlin.math.ceil
import kotlin.math.max

/**
 * Dispatches platform-owned collection work through Folia's global region scheduler.
 *
 * Reflection preserves UnifiedMetrics' Bukkit 1.8+ linkage while using the public Folia scheduler API when present.
 */
@OptIn(InternalCoroutinesApi::class)
class FoliaGlobalDispatcher(private val plugin: JavaPlugin) : CoroutineDispatcher(), Delay {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!context.isActive) {
            return
        }

        val scheduler = globalScheduler()
        scheduler.javaClass
            .getMethod("execute", Plugin::class.java, Runnable::class.java)
            .invoke(scheduler, plugin, block)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val scheduler = globalScheduler()
        val ticks = max(1L, ceil(timeMillis / MILLIS_PER_TICK).toLong())
        val task = scheduler.javaClass
            .getMethod("runDelayed", Plugin::class.java, Consumer::class.java, Long::class.javaPrimitiveType)
            .invoke(
                scheduler,
                plugin,
                Consumer<Any> { continuation.apply { resumeUndispatched(Unit) } },
                ticks
            )

        continuation.invokeOnCancellation {
            task.javaClass.getMethod("cancel").invoke(task)
        }
    }

    private fun globalScheduler(): Any =
        plugin.server.javaClass.getMethod("getGlobalRegionScheduler").invoke(plugin.server)

    private companion object {
        const val MILLIS_PER_TICK = 50.0
    }
}
