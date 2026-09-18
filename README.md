
![](src/main/resources/icon.png)
No Spawn Structures
---
An small mod to prevent structures from spawning with a configurable "exclusion zone" surrounding the world origin (x0 z0), or effectively spawn for most worlds.

Also allows whitelisting some structures so that they can still spawn inside the exclusion zone, or blacklisting some structures to prevent them from spawning inside the exclusion zone.

Particularly useful for preventing structures that are supposed to be rare spawning right next to spawn inside modpacks.

## Modloader Compatibility
This is a 1.21.1 NeoForge mod.

I have zero plans to port to Fabric. If you want a Fabric version, try out 7bbbbbbb's [No Structure at Spawn](https://github.com/7bbbbbbb/no_structure_at_spawn) ([Curse](https://www.curseforge.com/minecraft/mc-mods/no-structure-at-spawn)/[Modrinth](https://modrinth.com/mod/no-structure-at-spawn))

I might backport/forwardport to other major modpack versions. Keyword might.

## Compatibility
As far as I'm aware, there aren't any compatibility problems.\
The immediate suspects ([C2ME](https://github.com/RelativityMC/C2ME-fabric) & [Moonrise](https://github.com/Tuinity/Moonrise)) are both fully compatible (at time of writing.)\
Note that I have not tested the C2ME OpenCL Acceleration Module (I use a GCN Generation AMD GPU.) and cannot ensure compatibility.

If you find any incompatibilities please do open an issue. I can't fix what I don't know about.

## Configuring
The mod loads its configurations from `nospawnstructs.toml` in the config directory.\
Every key listed here is also documented inside the config file itself.
- `CONFIG_VERSION` (Integer): Currently `1`.\
Used by the mod to determine if your config exists.\
Don't touch it, at the moment it **will** wipe your configuration if it's less than the current verison.
- `LOGGING_LEVEL` (Integer): Ranges between `0` and `3`. Defaults to `1`.\
Determines how much the mod pollutes your log file.
	- `0`: Absolutely nothing.
	- `1`: Will warn when it overwrites an invalid config value.
	- `2`: Will dump the current configuration on startup, and log whenever it prevents a structure from spawning.
	- `3`: Will additionally log whenever it finds a structure, but doesn't prevent its spawning.
- `RADIUS` (Double): Must be positive. Defaults to `16.0`.\
Determines the radius of the exclusion zone.\
Is defined in Chunks, rather than blocks. The default 16 chunks is actually 256 blocks.
- `SHAPE` (String): Must be any of `"Circle", "Diamond", "Square"`. Defaults to `"Circle"`.\
Determines the function used for distance calculation, and by extension the shape of the exclusion zone.
	- `Circle`: `sqrt(x^2 + z^2)`
	- `Diamond`: `abs(x) + abs(z)`
	- `Square`: `max(abs(x),abs(z))`
- `BLACKLIST` (Boolean): Defaults to `false`.\
Determines if `STRUCTURE_LIST` is treated as a blacklist/disallowlist instead of a whitelist/allowlist.
- `STRUCTURE_LIST` (List\<String>): Defaults to `["minecraft:witch_hut"]`\
A special list of structure resource locations to either allow inside the exclusion zone (if `BLACKLIST` is `false`) or disallow (`BLACKLIST` is `true`).\
These are specifically the same ids used by the `/locate structure` command.

A word of caution, do not remove or change the types of these configs. The mod **will** hard crash your game with a cryptic `java.lang.IllegalStateException: Rendersystem called from wrong thread` followed by `java.lang.RuntimeException: java.lang.ExceptionInInitializerError`.

## Credits
The mod was built on top of the base [NeoForge 1.21.1 MDK](https://github.com/NeoForgeMDKs/MDK-1.21.1-NeoGradle).