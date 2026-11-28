package net.mesomods.refurbishedelectricity.mixin.ticker;

import com.mrcrayfish.furniture.refurbished.block.ElectricityGeneratorBlock;
import com.mrcrayfish.furniture.refurbished.block.FurnitureEntityBlock;
import com.mrcrayfish.furniture.refurbished.core.ModBlockEntities;
import net.mesomods.refurbishedelectricity.NodeTickers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ElectricityGeneratorBlock.class)
public class ElectricityGeneratorBlockMixin {
    @Inject(method = "getTicker", at = @At("RETURN"), cancellable = true)
    public <T extends BlockEntity> void getTicker(Level level, BlockState state, BlockEntityType<T> type, CallbackInfoReturnable<BlockEntityTicker<T>> cir) {
        if (!level.isClientSide()) {
            cir.setReturnValue(FurnitureEntityBlock.createTicker(type, ModBlockEntities.ELECTRICITY_GENERATOR.get(), NodeTickers::electricityGeneratorServerTick));
        }
    }
}
