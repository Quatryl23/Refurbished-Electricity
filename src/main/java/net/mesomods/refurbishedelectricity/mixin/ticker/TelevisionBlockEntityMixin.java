package net.mesomods.refurbishedelectricity.mixin.ticker;

import com.mrcrayfish.furniture.refurbished.blockentity.TelevisionBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TelevisionBlockEntity.class)
public interface TelevisionBlockEntityMixin {
    @Invoker(value = "specialTick", remap = false)
    void callSpecialTick();
}
