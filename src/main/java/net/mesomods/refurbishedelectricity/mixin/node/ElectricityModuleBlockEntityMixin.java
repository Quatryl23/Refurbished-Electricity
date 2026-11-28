package net.mesomods.refurbishedelectricity.mixin.node;

import com.mrcrayfish.furniture.refurbished.Config;
import com.mrcrayfish.furniture.refurbished.blockentity.*;
import com.mrcrayfish.furniture.refurbished.electricity.IElectricityNode;
import com.mrcrayfish.furniture.refurbished.electricity.IModuleNode;
import com.mrcrayfish.furniture.refurbished.electricity.ISourceNode;
import net.mesomods.refurbishedelectricity.mixinaccess.ElectricityModuleBlockEntityMixinAccess;
import net.mesomods.refurbishedelectricity.mixinaccess.ElectricitySourceBlockEntityMixinAccess;
import net.mesomods.refurbishedelectricity.mixinaccess.IElectricityNodeMixinAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin({ElectricityModuleBlockEntity.class, ElectricityModuleLootBlockEntity.class, ElectricityModuleContainerBlockEntity.class, ElectricityModuleProcessingLootBlockEntity.class})
public abstract class ElectricityModuleBlockEntityMixin implements ElectricityModuleBlockEntityMixinAccess, IModuleNode, IForgeBlockEntity {
    @Shadow(remap = false)
    @Final
    protected Set<BlockPos> powerSources;

    @Unique
    List<ISourceNode> refurbished_electricity$connectedSources = new ArrayList<>();

    @Override
    public void refurbished_electricity$removeFromNetwork(ISourceNode source, Level level) {
        this.refurbished_electricity$connectedSources.remove(source);
        this.powerSources.remove(source.getNodePosition());
        if (!level.hasChunk(SectionPos.blockToSectionCoord(this.getNodePosition().getX()), SectionPos.blockToSectionCoord(this.getNodePosition().getZ()))) return;
        if (this.refurbished_electricity$connectedSources.isEmpty()) {
            if (!Config.SERVER.electricity.cheats.everythingIsPowered.get()) {
                this.setNodePowered(false);
            }
            this.setNodeReceivingPower(false);
        }
        this.getNodeOwner().setChanged();
        ((IElectricityNodeMixinAccess) this).refurbished_electricity$syncPowerSourcesToTrackingClients();
    }

    @Override
    public void refurbished_electricity$addToNetwork(ISourceNode source, Level level) {
        this.refurbished_electricity$connectedSources.add(source);
        this.powerSources.add(source.getNodePosition());
        this.setNodePowered(true);
        this.setNodeReceivingPower(true);
        this.getNodeOwner().setChanged();
        ((IElectricityNodeMixinAccess) this).refurbished_electricity$syncPowerSourcesToTrackingClients();
    }

    @Unique
    public void refurbished_electricity$updateAllSources(List<ISourceNode> sources) {
        sources.forEach((source) -> ((ElectricitySourceBlockEntityMixinAccess)source).refurbished_electricity$updateNodeNetwork(source.getNodeLevel(), source.isNodePowered()));
    }

    @Override
    public void refurbished_electricity$updateAllSources() {
        this.refurbished_electricity$updateAllSources(this.refurbished_electricity$connectedSources);
    }

    @Override
    public void refurbished_electricity$searchNetworkAndUpdateSources() {
        List<IElectricityNode> foundSourceNodes = IElectricityNode.searchNodes(this, Config.SERVER.electricity.maximumNodesInNetwork.get(), false, (node) -> true, (node) -> (node instanceof ISourceNode));
        foundSourceNodes.forEach((node) -> ((ElectricitySourceBlockEntityMixinAccess) node).refurbished_electricity$updateNodeNetwork(node.getNodeLevel(), node.isNodePowered()));
    }

    @Unique
    public void refurbished_electricity$removeFromSources() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        this.refurbished_electricity$connectedSources.forEach((source) -> ((ElectricitySourceBlockEntityMixinAccess)source).refurbished_electricity$removeModule(source.getNodeLevel(), this, server != null && !server.isCurrentlySaving()));
        this.refurbished_electricity$connectedSources.clear();
        this.powerSources.clear();
        ((IElectricityNodeMixinAccess) this).refurbished_electricity$syncPowerSourcesToTrackingClients();
    }

    @Override
    public void onNodeDestroyed() {
        ((IElectricityNodeMixinAccess) this).refurbished_electricity$removeAllNodeConnectionsSuppressNetworkUpdates();
        if (!this.refurbished_electricity$connectedSources.isEmpty() && !this.getNodeLevel().isClientSide()) this.refurbished_electricity$removeFromSources();
    }

    @Override
    public void onLoad() {
        if (Config.SERVER.electricity.cheats.everythingIsPowered.get() && !this.isNodePowered()) {
            this.setNodePowered(true);
            this.getNodeOwner().setChanged();
        } else if (this.isNodePowered() && this.refurbished_electricity$connectedSources.isEmpty()) {
            this.setNodePowered(false);
            this.getNodeOwner().setChanged();
        }
        ((IElectricityNodeMixinAccess) this).refurbished_electricity$syncPowerSourcesToTrackingClients();
        this.refurbished_electricity$searchNetworkAndUpdateSources();
    }

    @Override
    public void onChunkUnloaded() {
        if (!this.refurbished_electricity$connectedSources.isEmpty() && !this.getNodeLevel().isClientSide()) this.refurbished_electricity$removeFromSources();
    }
}
