package net.mesomods.refurbishedelectricity.mixin.node;

import com.mrcrayfish.furniture.refurbished.blockentity.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({LightswitchBlockEntity.class, CeilingFanBlockEntity.class, ComputerBlockEntity.class, ElectricityGeneratorBlockEntity.class, LightingBlockEntity.class, RangeHoodBlockEntity.class, StoveBlockEntity.class, TelevisionBlockEntity.class, WorkbenchBlockEntity.class})
public class PoweredBlockEntityMixin extends BlockEntity {
    public PoweredBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Redirect(method = "setNodePowered", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), remap = false)
    public boolean setNodePowered(Level level, BlockPos pos, BlockState state, int flag) {
        if (this.getBlockState().getValue(BlockStateProperties.POWERED) != state.getValue(BlockStateProperties.POWERED)) {
            return level.setBlock(pos, state, flag);
        }
        return false;
    }
}
