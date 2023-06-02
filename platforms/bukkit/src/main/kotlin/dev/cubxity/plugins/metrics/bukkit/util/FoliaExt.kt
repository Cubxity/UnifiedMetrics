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

package dev.cubxity.plugins.metrics.bukkit.util

import io.papermc.paper.threadedregions.ThreadedRegionizer
import io.papermc.paper.threadedregions.TickRegions.TickRegionData
import io.papermc.paper.threadedregions.TickRegions.TickRegionSectionData
import org.bukkit.World
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld

val World.regioniser: ThreadedRegionizer<TickRegionData, TickRegionSectionData>
    get() = (this as CraftWorld).handle.regioniser
