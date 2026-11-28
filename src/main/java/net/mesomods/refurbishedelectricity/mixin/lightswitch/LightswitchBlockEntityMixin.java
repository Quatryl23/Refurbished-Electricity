package net.mesomods.refurbishedelectricity.mixin.lightswitch;

import com.mrcrayfish.furniture.refurbished.blockentity.LightswitchBlockEntity;
import net.mesomods.refurbishedelectricity.mixinaccess.ElectricityModuleBlockEntityMixinAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightswitchBlockEntity.class)
public abstract class LightswitchBlockEntityMixin implements ElectricityModuleBlockEntityMixinAccess {
    @Inject(method = "toggleDeviceState", at = @At("TAIL"), remap = false)
    public void toggleDeviceStateUpdate(CallbackInfo ci) {
        this.refurbished_electricity$updateAllSources();
    }

    @Inject(method = "setDeviceState", at = @At("TAIL"), remap = false)
    public void setDeviceStateUpdate(boolean enabled, CallbackInfo ci) {
        this.refurbished_electricity$updateAllSources();
    }
}
