package net.mesomods.refurbishedelectricity.mixin.stove;

import com.mrcrayfish.furniture.refurbished.blockentity.StoveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StoveBlockEntity.class)
public class StoveBlockEntityMixin {
    @Inject(method = "serverTick", at = @At("HEAD"), cancellable = true, remap = false)
    private static void cancelTickIfNotProcessing(Level level, BlockPos pos, BlockState state, StoveBlockEntity stove, CallbackInfo ci) {
        if (!stove.isProcessing() && stove.isEmpty()) {
            ci.cancel();
        }
    }
}
