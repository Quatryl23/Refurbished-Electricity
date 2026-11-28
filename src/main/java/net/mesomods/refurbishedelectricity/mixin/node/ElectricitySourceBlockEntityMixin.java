package net.mesomods.refurbishedelectricity.mixin.node;

import com.mrcrayfish.furniture.refurbished.blockentity.ElectricitySourceBlockEntity;
import com.mrcrayfish.furniture.refurbished.blockentity.ElectricitySourceContainerBlockEntity;
import com.mrcrayfish.furniture.refurbished.blockentity.ElectricitySourceLootBlockEntity;
import com.mrcrayfish.furniture.refurbished.electricity.IElectricityNode;
import com.mrcrayfish.furniture.refurbished.electricity.IModuleNode;
import com.mrcrayfish.furniture.refurbished.electricity.ISourceNode;
import com.mrcrayfish.furniture.refurbished.electricity.NodeSearchResult;
import net.mesomods.refurbishedelectricity.mixinaccess.ElectricityModuleBlockEntityMixinAccess;
import net.mesomods.refurbishedelectricity.mixinaccess.ElectricitySourceBlockEntityMixinAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin({ElectricitySourceBlockEntity.class, ElectricitySourceLootBlockEntity.class, ElectricitySourceContainerBlockEntity.class})
public abstract class ElectricitySourceBlockEntityMixin implements ElectricitySourceBlockEntityMixinAccess, ISourceNode, IForgeBlockEntity {
    @Unique
    List<IModuleNode> refurbished_electricity$connectedModules = new ArrayList<>();

    @Override
    public void refurbished_electricity$updateNodeNetwork(Level level, boolean isPowered) {
        if (isPowered) {
            this.refurbished_electricity$updateNodeNetwork(level);
        } else {
            this.refurbished_electricity$disassembleNodeNetwork(level, true);
        }
    }

    @Override
    public void refurbished_electricity$updateNodeNetwork(Level level) {
        if (level.isClientSide()) return;
        List<IModuleNode> removedModules = new ArrayList<>(refurbished_electricity$connectedModules);
        List<IModuleNode> addedModules = new ArrayList<>();
        NodeSearchResult result = this.searchNodeNetwork(false);
        if (result.overloaded()) {
            this.setNodeOverloaded(true);
            this.onNodeOverloaded();
            this.getNodeOwner().setChanged();
            return;
        }
        for (IElectricityNode node: result.nodes()) {
            if (node instanceof IModuleNode module) {
                // if the found module was connected before, it is removed from removedModules, if it was not connected, it is a newly added module and is added to addedModules.
                if (!removedModules.remove(module)) addedModules.add(module);
            }
        }
        for (IModuleNode module: removedModules) {
            this.refurbished_electricity$connectedModules.remove(module);
            ((ElectricityModuleBlockEntityMixinAccess) module).refurbished_electricity$removeFromNetwork(this, level);
        }
        for (IModuleNode module: addedModules) {
            this.refurbished_electricity$connectedModules.add(module);
            ((ElectricityModuleBlockEntityMixinAccess) module).refurbished_electricity$addToNetwork(this, level);
        }
        this.getNodeOwner().setChanged();
    }

    @Override
    public void refurbished_electricity$disassembleNodeNetwork(Level level, boolean stillExisting) {
        if (level.isClientSide()) return;
        for (IModuleNode module: refurbished_electricity$connectedModules) {
            ((ElectricityModuleBlockEntityMixinAccess) module).refurbished_electricity$removeFromNetwork(this, level);
        }
        this.refurbished_electricity$connectedModules.clear();
        if (stillExisting) this.getNodeOwner().setChanged();
    }

    @Override
    public void refurbished_electricity$removeModule(Level level, IModuleNode module, boolean updateNetwork) {
        this.refurbished_electricity$connectedModules.remove(module);
        if (updateNetwork) {
            List<IElectricityNode> remainingNodes = IElectricityNode.searchNodes(this, this.getMaxPowerableNodes(), false, (node) -> !node.isSourceNode() && node.canPowerTraverseNode() && node != module, (node) -> !node.isSourceNode());
            this.refurbished_electricity$connectedModules.removeIf(node -> {
                if (!remainingNodes.contains(node)) {
                    ((ElectricityModuleBlockEntityMixinAccess) node).refurbished_electricity$removeFromNetwork(this, level);
                    return true;
                }
                return false;
            });
        }
        this.getNodeOwner().setChanged();
    }

    @Override
    public void onNodeDestroyed() {
        this.removeAllNodeConnections();
        this.refurbished_electricity$disassembleNodeNetwork(this.getNodeLevel(), false);
    }

    @Override
    public void onLoad() {
        this.refurbished_electricity$updateNodeNetwork(this.getNodeLevel(), this.isNodePowered());
    }

    @Override
    public void onChunkUnloaded() {
        this.refurbished_electricity$disassembleNodeNetwork(this.getNodeLevel(), false);
    }
}