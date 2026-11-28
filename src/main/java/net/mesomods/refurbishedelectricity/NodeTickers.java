package net.mesomods.refurbishedelectricity;

import com.mrcrayfish.furniture.refurbished.blockentity.*;
import com.mrcrayfish.furniture.refurbished.client.audio.AudioManager;
import net.mesomods.refurbishedelectricity.mixin.ticker.CeilingFanBlockEntityMixin;
import net.mesomods.refurbishedelectricity.mixin.ticker.ComputerBlockEntityMixin;
import net.mesomods.refurbishedelectricity.mixin.ticker.MicrowaveBlockEntityMixin;
import net.mesomods.refurbishedelectricity.mixin.ticker.TelevisionBlockEntityMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NodeTickers {
    public static void electricityGeneratorServerTick(Level level, BlockPos pos, BlockState state, ElectricityGeneratorBlockEntity generator) {
        if (level.getGameTime() % Config.SERVER.ticksPerGeneratorTick.get() == 0) {
            generator.processTick();
        }
    }

    public static void ceilingFanServerTick(Level level, BlockPos pos, BlockState state, CeilingFanBlockEntity ceilingFan) {
        ((CeilingFanBlockEntityMixin) ceilingFan).callPerformDamage(level);
    }

    public static void ceilingFanClientTick(Level level, BlockPos pos, BlockState state, CeilingFanBlockEntity ceilingFan) {
        ((CeilingFanBlockEntityMixin) ceilingFan).callUpdateAnimation();
        AudioManager.get().playLevelAudio(ceilingFan);
    }

    public static void computerServerTick(Level level, BlockPos pos, BlockState state, ComputerBlockEntity computer) {
        ((ComputerBlockEntityMixin) computer).callTickProgram();
    }

    public static void microwaveClientTick(Level level, BlockPos pos, BlockState state, MicrowaveBlockEntity microwave) {
        AudioManager.get().playLevelAudio(microwave);
    }

    public static void microwaveServerTick(Level level, BlockPos pos, BlockState state, MicrowaveBlockEntity microwave) {
        boolean processing = microwave.processTick();
        if (((MicrowaveBlockEntityMixin) microwave).isProcessing() != processing) {
            ((MicrowaveBlockEntityMixin) microwave).setProcessing(processing);
            microwave.syncDataToTrackingClients();
        }
    }

    public static void televisionClientTick(Level level, BlockPos pos, BlockState state, TelevisionBlockEntity television) {
        AudioManager.get().playLevelAudio(television);
    }

    public static void televisionServerTick(Level level, BlockPos pos, BlockState state, TelevisionBlockEntity television) {
        ((TelevisionBlockEntityMixin) television).callSpecialTick();
    }
}