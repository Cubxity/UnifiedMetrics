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

package dev.cubxity.plugins.metrics.sponge;

import com.google.inject.Inject;
import dev.cubxity.plugins.metrics.sponge.bootstrap.UnifiedMetricsSpongeBootstrap;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;

@Plugin(
        id = "umetrics",
        name = "UnifiedMetrics",
        version = "0.2.1",
        description = "Fully-featured metrics plugin for Minecraft servers"
)
public class UMSpongePlugin {

    private final UnifiedMetricsSpongePlugin plugin;

    @Inject
    public UMSpongePlugin(Logger internalLogger, @ConfigDir(sharedRoot = false) Path configDir) {
        plugin = new UnifiedMetricsSpongePlugin(
                new UnifiedMetricsSpongeBootstrap(internalLogger, configDir));
    }

    @Listener
    public void onEnable(GameStartedServerEvent event) {
        plugin.enable();
    }

    @Listener
    public void onDisable(GameStoppedEvent event) {
        plugin.disable();
    }
}
