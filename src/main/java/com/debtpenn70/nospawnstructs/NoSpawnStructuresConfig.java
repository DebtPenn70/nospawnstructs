package com.debtpenn70.nospawnstructs;

import java.util.Optional;
import java.util.List;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfigBuilder;
import com.electronwill.nightconfig.core.file.GenericBuilder;

// TODO: If a config property is set to the wrong class or is just outright missing the game will genuinely just crash.

public class NoSpawnStructuresConfig {
	public static Integer CONFIG_VERSION = 1;

	public static CommentedFileConfig init(){
		GenericBuilder<CommentedConfig,CommentedFileConfig> cbuild = CommentedFileConfig.builder("config/nospawnstructs.toml").sync();
		// FileConfigBuilder cbuild = FileConfig.builder("config/nospawnstructs.toml").sync();
		CommentedFileConfig config = cbuild.build();
		config.load();

		Optional<Integer> version = config.getOptional("CONFIG_VERSION");
		if(version.isPresent()){
			Integer version_act = version.get();
			if(version_act < CONFIG_VERSION){
				writeNew(config);
			}else{
				// Validation
				Integer logging_level = config.get("LOGGING_LEVEL");
				if(logging_level < 0){
					NoSpawnStructures.LOGGER.warn("Config property LOGGING_LEVEL was set out of bounds [0-3]! Reset to 1.");
					config.set("LOGGING_LEVEL",1);
					logging_level = 1;
				}
				if(logging_level > 3){
					NoSpawnStructures.LOGGER.warn("Config property LOGGING_LEVEL was set out of bounds [0-3]! Reset to 1.");
					config.set("LOGGING_LEVEL",1);
					logging_level = 1;
				}

				String shape = config.get("SHAPE");
				if(!List.of("Circle","Square","Diamond").contains(shape)){
					if(logging_level > 0){
						NoSpawnStructures.LOGGER.warn("Config property SHAPE was set to an invalid value! Reset to \"Circle\"");
					}
					config.set("SHAPE","Circle");
				}

				Double radius = config.get("RADIUS");
				if(radius < 0){
					if(logging_level > 0){
						NoSpawnStructures.LOGGER.warn("Config property RADIUS was set to a negative value! Reset to 16.0");
					}
					config.set("RADIUS",16.0d);
				}
			}
		}else{
			writeNew(config);
		}

		config.save();
		return config;
	}

	public static void writeNew(CommentedFileConfig config){
		config.set("CONFIG_VERSION", CONFIG_VERSION);
		config.setComment("CONFIG_VERSION","\n Don't touch this. May be used in the future.");

		config.set("RADIUS", 16.0d);
		config.setComment("RADIUS","\n The radius to prevent structures spawning in.\n Is specified in chunks, multiply by 16 to get the equivilent blocks.");

		config.set("SHAPE","Circle");
		config.setComment("SHAPE","\n The shape of the exclusion zone. Can be one of three:" +
		"\n	\"Circle\"  : A circle.	sqrt(x^2 + z^2)" +
		"\n	\"Square\"  : A square.	max(abs(x),abs(z))" +
		"\n	\"Diamond\" : A diamond	abs(x)+abs(z)");

		config.set("BLACKLIST",false);
		config.setComment("BLACKLIST","\n Whether STRUCTURE_LIST is a blacklist. If false, STRUCTURE_LIST is treated as a whitelist.");

		config.set("LOGGING_LEVEL",1);
		config.setComment("LOGGING_LEVEL","\n Determines how much the mod will spit out to your log file." + 
		"\n	0: Absolutely nothing." + 
		"\n	1: Will log a warning if it overwrites an invalid config value." + 
		"\n	2: Will dump the current config on startup and list whenever it blocks the spawning of a structure." + 
		"\n	3: Will also list whenever it could've, but didn't block the spawning of a structure.");

		config.set("STRUCTURE_LIST", List.of("minecraft:witch_hut"));
		config.setComment("STRUCTURE_LIST","\n A list of structures to either allow through (BLACKLIST = false) or to prevent (BLACKLIST = true)\n Takes the form of the structure resource location, or the same id you use when using /locate.\n Does not work with tags.");
	}
}