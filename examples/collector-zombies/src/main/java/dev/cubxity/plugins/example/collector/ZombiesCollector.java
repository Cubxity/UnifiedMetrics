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

package dev.cubxity.plugins.example.collector;

import dev.cubxity.plugins.metrics.api.metric.collector.Collector;
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric;
import dev.cubxity.plugins.metrics.api.metric.data.Metric;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Zombie;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZombiesCollector implements Collector {
    private final Map<String, String> EMPTY_LABELS = new HashMap<>();

    @NotNull
    @Override
    public List<Metric> collect() {
        // This can negatively impact the performance.
        // DO NOT use in production.
        int count = 0;
        for (World world : Bukkit.getWorlds()) {
            count += world.getEntitiesByClass(Zombie.class).size();
        }

        GaugeMetric metric = new GaugeMetric("minecraft_zombies_count", EMPTY_LABELS, count);
        return Collections.singletonList(metric);
    }
}
