package net.mesomods.refurbishedelectricity.mixinaccess;

import com.mrcrayfish.furniture.refurbished.electricity.ISourceNode;
import net.minecraft.world.level.Level;

public interface ElectricityModuleBlockEntityMixinAccess {
    void refurbished_electricity$removeFromNetwork(ISourceNode source, Level level);

    void refurbished_electricity$addToNetwork(ISourceNode source, Level level);

    void refurbished_electricity$updateAllSources();

    void refurbished_electricity$searchNetworkAndUpdateSources();
}
