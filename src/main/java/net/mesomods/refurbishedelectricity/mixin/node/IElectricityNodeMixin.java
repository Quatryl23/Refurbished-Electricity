package net.mesomods.refurbishedelectricity.mixin.node;

import com.mrcrayfish.furniture.refurbished.electricity.Connection;
import com.mrcrayfish.furniture.refurbished.electricity.IElectricityNode;
import com.mrcrayfish.furniture.refurbished.electricity.IModuleNode;
import com.mrcrayfish.furniture.refurbished.electricity.ISourceNode;
import com.mrcrayfish.furniture.refurbished.util.BlockEntityHelper;
import net.mesomods.refurbishedelectricity.mixinaccess.ElectricityModuleBlockEntityMixinAccess;
import net.mesomods.refurbishedelectricity.mixinaccess.ElectricitySourceBlockEntityMixinAccess;
import net.mesomods.refurbishedelectricity.mixinaccess.IElectricityNodeMixinAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin(IElectricityNode.class)
public interface IElectricityNodeMixin extends IElectricityNodeMixinAccess {

    @Shadow(remap = false)
    Set<Connection> getNodeConnections();
    @Shadow(remap = false)
    void syncDataToTrackingClients();
    @Shadow(remap = false)
    BlockEntity getNodeOwner();
    @Shadow(remap = false)
    BlockPos getNodePosition();
    @Shadow(remap = false)
    Set<BlockPos> getPowerSources();

    /**
     * @author Mesophil
     * @reason Mixin limitations: Interface default method injection is not supported
     */
    @Overwrite(remap = false)
    default void onNodeConnectedTo(IElectricityNode other) {
        if (this instanceof IModuleNode module) {
            ((ElectricityModuleBlockEntityMixinAccess) module).refurbished_electricity$searchNetworkAndUpdateSources();
        } else if (other instanceof IModuleNode module) {
            ((ElectricityModuleBlockEntityMixinAccess) module).refurbished_electricity$searchNetworkAndUpdateSources();
        } else if (this instanceof ISourceNode source) {
            ((ElectricitySourceBlockEntityMixinAccess) source).refurbished_electricity$updateNodeNetwork(source.getNodeLevel(), source.isNodePowered());
        } else if (other instanceof ISourceNode source) {
            ((ElectricitySourceBlockEntityMixinAccess) source).refurbished_electricity$updateNodeNetwork(source.getNodeLevel(), source.isNodePowered());
        }
    }

    /**
     * @author Mesophil
     * @reason Mixin limitations: Interface default method injection is not supported
     */
    @Overwrite(remap = false)
    default void removeNodeConnection(Connection connection) {
        if (this.getNodeConnections().remove(connection)) {
            this.syncDataToTrackingClients();
            this.getNodeOwner().setChanged();
        }
        if (this instanceof IModuleNode module) {
            ((ElectricityModuleBlockEntityMixinAccess) module).refurbished_electricity$searchNetworkAndUpdateSources();
        } else if (this instanceof ISourceNode source) {
            ((ElectricitySourceBlockEntityMixinAccess) source).refurbished_electricity$updateNodeNetwork(source.getNodeLevel(), source.isNodePowered());
        }
    }

    @Unique
    default void refurbished_electricity$removeNodeConnectionSuppressNetworkUpdate(Connection connection) {
        if (this.getNodeConnections().remove(connection)) {
            this.syncDataToTrackingClients();
            this.getNodeOwner().setChanged();
        }
    }

    @Override
    default void refurbished_electricity$removeAllNodeConnectionsSuppressNetworkUpdates() {
        Set<Connection> connections = this.getNodeConnections();
        connections.forEach((c) -> {
            IElectricityNode node = c.getOtherNode((IElectricityNode) this);
            if (node != null && node.isNodeValid()) {
                ((IElectricityNodeMixin) node).refurbished_electricity$removeNodeConnectionSuppressNetworkUpdate(c);
                node.syncDataToTrackingClients();
            }

        });
        connections.clear();
        this.getNodeOwner().setChanged();
    }

    /**
     * @author Mesophil
     * @reason Mixin limitations: Interface default method injection is not supported
     */
    @Overwrite(remap = false)
    default void readNodeNbt(CompoundTag tag) {
        if (tag.contains("Connections", 12)) {
            BlockPos offset = BlockPos.ZERO;
            if (tag.contains("NodePos", 4)) {
                BlockPos current = this.getNodePosition();
                BlockPos previous = BlockPos.of(tag.getLong("NodePos"));
                if (!current.equals(previous)) {
                    offset = current.subtract(previous);
                }
            }

            BlockPos pos = this.getNodePosition();
            Set<Connection> connections = this.getNodeConnections();
            connections.clear();
            long[] nodes = tag.getLongArray("Connections");

            for(long node : nodes) {
                connections.add(Connection.of(pos, BlockPos.of(node).offset(offset)));
            }
        } else if (tag.contains("PowerSources")) {
            Set<BlockPos> powerSources = this.getPowerSources();
            powerSources.clear();
            long[] blockPosLongs = tag.getLongArray("PowerSources");
            for (long blockPosLong : blockPosLongs) {
                powerSources.add(BlockPos.of(blockPosLong));
            }
        }
    }

    @Unique
    default CompoundTag refurbished_electricity$getPowerSourcesNbt() {
        CompoundTag tag = new CompoundTag();
        Set<BlockPos> powerSources = this.getPowerSources();
        List<Long> blockPosLongs = new ArrayList<>();
        for (BlockPos powerSource : powerSources) {
            blockPosLongs.add(powerSource.asLong());
        }
        tag.putLongArray("PowerSources", blockPosLongs);
        return tag;
    }

    @Override
    default void refurbished_electricity$syncPowerSourcesToTrackingClients() {
        BlockEntity nodeOwner = this.getNodeOwner();
        BlockEntityHelper.sendCustomUpdate(nodeOwner, this.refurbished_electricity$getPowerSourcesNbt());
    }
}
