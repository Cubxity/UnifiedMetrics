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

package dev.cubxity.plugins.metrics.fabric.mixins;

import dev.cubxity.plugins.metrics.fabric.events.TickEvent;
import net.minecraft.server.MinecraftServer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.cubxity.plugins.metrics.api.metric.collector.CollectorKt.NANOSECONDS_PER_MILLISECOND;
import static dev.cubxity.plugins.metrics.api.metric.collector.CollectorKt.NANOSECONDS_PER_SECOND;

/**
 * Designed to emulate paper's tick event as closely as possible
 */
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    private long lastTick = 0;

    @Inject(
        method = "runServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;setFavicon(Lnet/minecraft/server/ServerMetadata;)V"
        )
    )
    private void onRunServerBeforeLoop(CallbackInfo ci) {
        lastTick = System.nanoTime() - ((long) NANOSECONDS_PER_SECOND / 20);
    }

    @Inject(
        method = "runServer",
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/server/MinecraftServer;debugStart:Lnet/minecraft/server/MinecraftServer$DebugStart;"
            )
        ),
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/server/MinecraftServer;timeReference:J",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void onRunServerBeforeTick(CallbackInfo ci) {
        lastTick = System.nanoTime();
    }

    @Inject(
        method = "tick",
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/util/snooper/Snooper;update()V"
            ),
            to = @At(
                value = "FIELD",
                target = "Lnet/minecraft/server/MinecraftServer;lastTickLengths:[J"
            )
        ),
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiler/Profiler;pop()V"
        )
    )
    private void onTickEnd(CallbackInfo ci) {
        TickEvent.Companion.getEVENT().invoker().onTick((double)(System.nanoTime() - lastTick) / NANOSECONDS_PER_MILLISECOND);
    }

}
