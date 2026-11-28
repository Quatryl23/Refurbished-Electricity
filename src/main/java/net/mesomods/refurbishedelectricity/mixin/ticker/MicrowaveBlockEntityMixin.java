package net.mesomods.refurbishedelectricity.mixin.ticker;

import com.mrcrayfish.furniture.refurbished.blockentity.MicrowaveBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MicrowaveBlockEntity.class)
public interface MicrowaveBlockEntityMixin {
    @Accessor(value = "processing", remap = false)
    boolean isProcessing();

    @Accessor(value = "processing", remap = false)
    void setProcessing(boolean processing);
}
