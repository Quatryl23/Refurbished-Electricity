package net.mesomods.refurbishedelectricity.mixinaccess;

public interface IElectricityNodeMixinAccess {
    void refurbished_electricity$removeAllNodeConnectionsSuppressNetworkUpdates();

    void refurbished_electricity$syncPowerSourcesToTrackingClients();
}
