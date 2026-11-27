# Refurbished Electricity - Minecraft Mod (Addon for MrCrayfish's Furniture Mod: Refurbished)

### Fast electricity networks

Refurbished Electricity reworks the ticking system for the electricity-related blocks in MrCrayfish's Furniture Mod: Refurbished.
Normally, power sources (Electricity Generators) rebuild their network of powered modules every tick. This heavily affects performance
with a large number of those blocks loaded. Refurbished Electricity caches the networks and rebuilds them only if something in the network
changes. Combined with additional small improvements, this can reduce the ticking time of MrCrayfish's Furniture Mod: Refurbished by up to 85%.

**But:** Frequent chunk loading and unloading can still impact performance. More inconsistent ticking times (possibly lag spikes) can occur.

### Power consumption per module [Configurable]

Refurbished Electricity changes the power consumption of Electricity Generators: It is dependent on the number of connected modules
instead of being a flat consumption per generator. Modules that are connected through a disabled Lightswitch will not count towards this number.
A Generator with 16 (configurable) connected modules will consume as much power as with the flat consumption. However, every Generator also consumes
power equal to the consumption of 1 module (configurable) additionally.

The flat consumption system from before can be re-enabled through the config file.

### Other changes

With Refurbished Electricity installed, the default of the config value `maximumNodesInNetwork` is changed from 64 to 512. This is mainly done to improve
compatibility with large networks generated in structures, like in [Mesophil's Cities](https://github.com/Quatryl23/Mesophils-Cities-Classic). You can still
change this value in the config file, just the initial value that is set when Refurbished Furniture is loaded for the first time will be different.

If you want further performance improvements, you can change the config value `ticksPerGeneratorTick`. This value controls the tick delay between two ticks
of Electricity Generators. The default value of 1 represents the normal behavior, ticking the Generators every game tick. You can change the value to decrease
the tick frequency, but this comes at the cost of precision. The tick generators run out of power can be delayed by this.

### When to use?
Refurbished Electricity is recommended if
- you play in worlds with many/large electricity networks loaded (espacially with high render distance)
- you care a lot about performance
- you want to use [Power consumption per module](https://github.com/Quatryl23/Refurbished-Electricity/README.md#power-consumption-per-module-configurable)

Refurbished Electricity is not recommended if
- you want the electricity network to be 100% reliable (I can't guarantee that everything works functionally the same. If something doesn't, please [report](https://github.com/Quatryl23/Refurbished-Electricity/issues) the issue)
- you want full compatibility (I don't know any mod that affects the electricity system of MrCrayfish's Furniture Mod: Refurbished, but maybe there are some) 
