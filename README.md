# Refurbished Electricity - Minecraft Mod (Addon for MrCrayfish's Furniture Mod: Refurbished)

### Fast electricity networks

Refurbished Electricity reworks the ticking system for the electricity-related blocks in MrCrayfish's Furniture Mod: Refurbished.
Normally, power sources (Electricity Generators) rebuild their network of powered modules every tick. This heavily affects performance
with a large number of those blocks loaded. Refurbished Electricity caches the networks and rebuilds them only if something in the network
changes. This can reduce the ticking time of MrCrayfish's Furniture Mod: Refurbished by up to 85%.

**But:** Frequent chunk loading and unloading can still impact performance. More inconsistent ticking times (possibly lag spikes) can occur.

### Power consumption per module [Configurable]

### When to use?
Refurbished Electricity is recommended if
- you play in worlds with many/large electricity networks loaded (espacially with high render distance)
- you care a lot about performance
- you want to use [Power consumption per module](https://github.com/Quatryl23/Refurbished-Electricity/edit/main/README.md#power-consumption-per-module-configurable)

Refurbished Electricity is not recommended if
- you want the electricity network to be 100% reliable (I can't guarantee that everything works functionally the same. If something doesn't, please [report](https://github.com/Quatryl23/Refurbished-Electricity/issues) the issue)
- you want full compatibility (I don't know any mod that affects the electricity system of MrCrayfish's Furniture Mod: Refurbished, but maybe there are some) 
