package net.mesomods.refurbishedelectricity.mixin.ticker;

import com.mrcrayfish.furniture.refurbished.blockentity.ComputerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ComputerBlockEntity.class)
public interface ComputerBlockEntityMixin {
    @Invoker(value = "tickProgram", remap = false)
    void callTickProgram();
}
