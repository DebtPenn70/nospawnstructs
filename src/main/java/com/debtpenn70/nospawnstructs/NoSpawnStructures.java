package com.debtpenn70.nospawnstructs;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import java.util.List;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

import net.minecraft.world.level.ChunkPos;
import com.debtpenn70.nospawnstructs.NoSpawnStructuresConfig;


@Mod("nospawnstructs")
public class NoSpawnStructures {
	public static final Logger LOGGER = LogUtils.getLogger();
	public static CommentedFileConfig CONFIG = NoSpawnStructuresConfig.init();

	public static final double C_RADIUS;
	public static final boolean C_BLACKLIST;
	public static final String C_SHAPE;
	public static final List<String> C_STRUCTURE_LIST;
	public static final Integer C_LOGGING_LEVEL;

	public static double getDistance_Circle(ChunkPos chunkPos){
		return Math.sqrt( Math.pow(chunkPos.x,2) + Math.pow(chunkPos.z,2) );
	}

	public static double getDistance_Diamond(ChunkPos chunkPos){
		return Math.abs(chunkPos.x) + Math.abs(chunkPos.z);
	}

	public static double getDistance_Square(ChunkPos chunkPos){
		return Math.max( Math.abs(chunkPos.x), Math.abs(chunkPos.z) );
	}

	public NoSpawnStructures(IEventBus modEventBus, ModContainer modContainer) {}

	static {
		C_RADIUS = CONFIG.get("RADIUS");
		C_BLACKLIST = CONFIG.get("BLACKLIST");
		C_SHAPE = CONFIG.get("SHAPE");
		C_STRUCTURE_LIST = CONFIG.get("STRUCTURE_LIST");
		C_LOGGING_LEVEL = CONFIG.get("LOGGING_LEVEL");

		if(C_LOGGING_LEVEL > 1){
			LOGGER.info("NoSpawnStructures Config Dump:");
			LOGGER.info("RADIUS: " + Double.toString(C_RADIUS));
			LOGGER.info("SHAPE: " + C_SHAPE);
			LOGGER.info("BLACKLIST: " + Boolean.toString(C_BLACKLIST));
			LOGGER.info("STRUCTURE_LIST: " + C_STRUCTURE_LIST.toString());
			LOGGER.info("LOGGING_LEVEL: " + Integer.toString(C_LOGGING_LEVEL));
		}

		CONFIG.close();
	}
}