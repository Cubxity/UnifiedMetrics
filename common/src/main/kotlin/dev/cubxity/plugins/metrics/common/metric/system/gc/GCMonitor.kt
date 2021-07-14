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

package dev.cubxity.plugins.metrics.common.metric.system.gc

import com.sun.management.GarbageCollectionNotificationInfo
import dev.cubxity.plugins.metrics.api.metric.collector.Histogram
import dev.cubxity.plugins.metrics.api.metric.collector.MILLISECONDS_PER_SECOND
import javax.management.Notification
import javax.management.NotificationListener
import javax.management.openmbean.CompositeData

class GCMonitor(
    private val durationHistogram: Histogram,
    private val freedHistogram: Histogram
) : NotificationListener {
    override fun handleNotification(notification: Notification, handback: Any?) {
        if (notification.type != GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION) {
            return
        }

        val info = (notification.userData as? CompositeData)
            ?.let { GarbageCollectionNotificationInfo.from(it) }?.gcInfo ?: return

        durationHistogram += info.duration / MILLISECONDS_PER_SECOND

        var diff = 0L

        for (usage in info.memoryUsageBeforeGc.values) {
            diff += usage.used
        }
        for (usage in info.memoryUsageAfterGc.values) {
            diff -= usage.used
        }

        println(diff)

        freedHistogram += diff
    }
}