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

package dev.cubxity.plugins.metrics.sponge.mixins;

import dev.cubxity.plugins.metrics.sponge.events.TickEndEvent;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.cubxity.plugins.metrics.api.metric.collector.CollectorKt.NANOSECONDS_PER_MILLISECOND;
import static dev.cubxity.plugins.metrics.api.metric.collector.CollectorKt.NANOSECONDS_PER_SECOND;

/**
 * Designed to emulate paper's tick event as closely as possible.
 */
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Unique
    private long unifiedmetrics$lastTick = 0;

    @Inject(
        method = "runServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;buildServerStatus()Lnet/minecraft/network/protocol/status/ServerStatus;"
        )
    )
    private void onRunServerBeforeLoop(CallbackInfo ci) {
        unifiedmetrics$lastTick = System.nanoTime() - ((long) NANOSECONDS_PER_SECOND / 20);
    }

    @Inject(
        method = "tickServer",
        at = @At("HEAD")
    )
    private void onTickStart(CallbackInfo ci) {
        unifiedmetrics$lastTick = System.nanoTime();
    }

    @Inject(
        method = "tickServer",
        at = @At(
                value = "CONSTANT",
                target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V",
                args = "stringValue=tallying")
    )
    private void onTickEnd(CallbackInfo ci) {
        Sponge.eventManager().post(new TickEndEvent((double)(System.nanoTime() - unifiedmetrics$lastTick) / NANOSECONDS_PER_MILLISECOND, Cause.of(EventContext.empty(), Sponge.server())));
    }
}
