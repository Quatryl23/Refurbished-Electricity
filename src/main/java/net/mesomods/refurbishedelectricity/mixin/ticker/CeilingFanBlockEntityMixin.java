package net.mesomods.refurbishedelectricity.mixin.ticker;

import com.mrcrayfish.furniture.refurbished.blockentity.CeilingFanBlockEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CeilingFanBlockEntity.class)
public interface CeilingFanBlockEntityMixin {
    @Invoker(value = "updateAnimation", remap = false)
    void callUpdateAnimation();
    @Invoker(value = "performDamage", remap = false)
    void callPerformDamage(Level level);
}
