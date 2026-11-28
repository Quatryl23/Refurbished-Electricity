package net.mesomods.refurbishedelectricity;

import com.mrcrayfish.framework.api.config.*;

public class Config {
    @FrameworkConfig(
            id = "refurbished_electricity",
            name = "server",
            type = ConfigType.SERVER_SYNC
    )
    public static final Server SERVER = new Server();

    public static class Server {
        @ConfigProperty(
                name = "consumeEnergyPerModule",
                comment = "If enabled, the energy consumption of generators depends on the number of connected modules"
        )
        public final BoolProperty consumeEnergyPerModule = BoolProperty.create(true);
        @ConfigProperty(
                name = "consumeEnergyPerModuleMultiplier",
                comment = "If consumeEnergyPerModule is enabled, multiplies energy generated from fuels to account for the increased consumption by this factor"
        )
        public final IntProperty consumeEnergyPerModuleMultiplier = IntProperty.create(16);
        @ConfigProperty(
                name = "consumeEnergyPerModuleBaseConsumption",
                comment = "If consumeEnergyPerModule is enabled, adds this value to the number of modules to calculate the energy consumption"
        )
        public final IntProperty consumeEnergyPerModuleBaseConsumption = IntProperty.create(1);
        @ConfigProperty(
                name = "ticksPerGeneratorTick",
                comment = "How many ticks it takes for Electricity Generators to tick once.\nHigher values increase performance, but decrease precision of Generator energy consumption.\nValues above 100 are discouraged because they can lead to unexpected behaviors"
        )
        public final IntProperty ticksPerGeneratorTick = IntProperty.create(1);
    }
}
