package net.mesomods.refurbishedelectricity.mixin;

import com.mrcrayfish.framework.api.config.IntProperty;
import com.mrcrayfish.furniture.refurbished.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Config.Server.Electricity.class)
public class ConfigMixin {
    // Increased node limit per network
    @Redirect(method = "<init>", remap = false, at = @At(value = "INVOKE", ordinal = 4, target = "Lcom/mrcrayfish/framework/api/config/IntProperty;create(III)Lcom/mrcrayfish/framework/api/config/IntProperty;"))
    private IntProperty replaceMaximumNodesInNetworkField(int defaultValue, int minValue, int maxValue) {
        return IntProperty.create(512, 1, 512);
    }
}
