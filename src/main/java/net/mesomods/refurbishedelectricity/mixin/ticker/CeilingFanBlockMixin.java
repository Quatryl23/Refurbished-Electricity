package net.mesomods.refurbishedelectricity.mixin.ticker;

import com.mrcrayfish.furniture.refurbished.block.CeilingFanBlock;
import com.mrcrayfish.furniture.refurbished.block.FurnitureEntityBlock;
import com.mrcrayfish.furniture.refurbished.core.ModBlockEntities;
import net.mesomods.refurbishedelectricity.NodeTickers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(CeilingFanBlock.class)
public abstract class CeilingFanBlockMixin implements EntityBlock {
    @Override @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
            return FurnitureEntityBlock.createTicker(type, ModBlockEntities.CEILING_FAN.get(), (level.isClientSide() ? NodeTickers::ceilingFanClientTick : NodeTickers::ceilingFanServerTick));
    }
}
