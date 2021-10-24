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
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ZombiesCollectorCollection implements CollectorCollection {
    private final List<Collector> collectors = Collections.singletonList(new ZombiesCollector());

    @NotNull
    @Override
    public List<Collector> getCollectors() {
        return this.collectors;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void initialize() {
        // Do nothing
    }

    @Override
    public void dispose() {
        // Do nothing
    }
}
