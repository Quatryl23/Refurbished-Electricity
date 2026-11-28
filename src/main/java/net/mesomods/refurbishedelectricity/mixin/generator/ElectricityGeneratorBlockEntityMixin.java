package net.mesomods.refurbishedelectricity.mixin.generator;

import com.mrcrayfish.framework.api.config.IntProperty;
import com.mrcrayfish.furniture.refurbished.blockentity.ElectricityGeneratorBlockEntity;
import com.mrcrayfish.furniture.refurbished.blockentity.IProcessingBlock;
import com.mrcrayfish.furniture.refurbished.electricity.ISourceNode;
import net.mesomods.refurbishedelectricity.Config;
import net.mesomods.refurbishedelectricity.mixinaccess.ElectricitySourceBlockEntityMixinAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ElectricityGeneratorBlockEntity.class)
public abstract class ElectricityGeneratorBlockEntityMixin implements ElectricitySourceBlockEntityMixinAccess, ISourceNode, IProcessingBlock {
    @Shadow(remap = false)
    protected int nodeCount;

    @Inject(method = "lambda$new$0", at = @At("RETURN"), cancellable = true, remap = false)
    public void hideNegativeEnergyValues(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Math.max(0, cir.getReturnValue()));
    }

    @Inject(method = "setNodePowered", at = @At("TAIL"), remap = false)
    public void updateOnNodePowerChange(boolean powered, CallbackInfo ci) {
        this.refurbished_electricity$updateNodeNetwork(this.getNodeLevel(), powered);
    }

    @Redirect(method = "retrieveEnergy", at = @At(value = "INVOKE", target = "Lcom/mrcrayfish/framework/api/config/IntProperty;get()Ljava/lang/Object;"), remap = false)
    public Object multiplyRetrievedEnergy(IntProperty instance) {
        return instance.get() * (Config.SERVER.consumeEnergyPerModule.get() ? Config.SERVER.consumeEnergyPerModuleMultiplier.get() : 1);
    }

    @Override
    public boolean processTick() {
        if (this.canProcess()) {
            if (this.getEnergy() <= 0 && this.retrieveEnergy(true) > 0) {
                this.addEnergy(this.retrieveEnergy(false));
            }
            if (this.getEnergy() > 0) {
                this.addEnergy((Config.SERVER.consumeEnergyPerModule.get() ? - (this.nodeCount + Config.SERVER.consumeEnergyPerModuleBaseConsumption.get()) : -1) * Config.SERVER.ticksPerGeneratorTick.get());
                if (!this.isNodePowered()) {
                    this.setNodePowered(true);
                }
                return true;
            } else if (this.isNodePowered()) {
                this.setNodePowered(false);
            }
        }
        if (this.isNodePowered()) {
            this.setNodePowered(false);
        }
        return false;
    }
}
