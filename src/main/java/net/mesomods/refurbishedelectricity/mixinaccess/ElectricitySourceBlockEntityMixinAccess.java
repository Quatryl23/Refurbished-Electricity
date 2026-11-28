package net.mesomods.refurbishedelectricity.mixinaccess;

import com.mrcrayfish.furniture.refurbished.electricity.IModuleNode;
import net.minecraft.world.level.Level;

public interface ElectricitySourceBlockEntityMixinAccess {
    void refurbished_electricity$updateNodeNetwork(Level level, boolean isPowered);

    void refurbished_electricity$updateNodeNetwork(Level level);

    void refurbished_electricity$disassembleNodeNetwork(Level level, boolean stillExisting);

    void refurbished_electricity$removeModule(Level level, IModuleNode module, boolean updateNetwork);
}
