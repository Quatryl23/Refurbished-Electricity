package net.mesomods.refurbishedelectricity.mixin;

import com.mrcrayfish.furniture.refurbished.electricity.ElectricityTicker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElectricityTicker.class)
public abstract class ElectricityTickerMixin {
    @Inject(method = "earlyTick", at = @At("HEAD"), cancellable = true, remap = false)
	private void refurbished_electricity$disableEarlyTick(CallbackInfo ci) {
        ci.cancel();

	}
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true, remap = false)
	private void refurbished_electricity$disableTick(CallbackInfo ci) {
        ci.cancel();
	}
}