package net.mesomods.refurbishedelectricity.mixin.stove;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.mrcrayfish.furniture.refurbished.blockentity.StoveBlockEntity$CookingSpace")
public class StoveBlockEntityCookingSpaceMixin {
    @Shadow(remap = false)
    private int bakingTime;

    @Inject(method = "setProcessingTime", at = @At("HEAD"), cancellable = true, remap = false)
    public void skipIfProcessingTimeUnchanged(int time, CallbackInfo ci) {
        if (this.bakingTime == time) {
            ci.cancel();
        }
    }
}
