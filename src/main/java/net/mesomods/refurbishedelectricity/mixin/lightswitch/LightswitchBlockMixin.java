package net.mesomods.refurbishedelectricity.mixin.lightswitch;

import com.mrcrayfish.furniture.refurbished.block.LightswitchBlock;
import net.mesomods.refurbishedelectricity.mixinaccess.ElectricityModuleBlockEntityMixinAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightswitchBlock.class)
public class LightswitchBlockMixin {
    @Inject(method = "use", at = @At("TAIL"))
    public void updateOnUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> cir) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be == null) return;
        ((ElectricityModuleBlockEntityMixinAccess) be).refurbished_electricity$updateAllSources();
    }
}
