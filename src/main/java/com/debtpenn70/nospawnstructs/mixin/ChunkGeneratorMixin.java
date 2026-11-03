package com.debtpenn70.nospawnstructs.mixin;

import com.debtpenn70.nospawnstructs.NoSpawnStructures;
import org.spongepowered.asm.mixin.Mixin;
import java.util.List;

import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.StructureSet.StructureSelectionEntry;
import net.minecraft.world.level.StructureManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {

	private static String SHAPE = NoSpawnStructures.C_SHAPE;
	private static double RADIUS = NoSpawnStructures.C_RADIUS;
	private static List<String> STRUCTURE_LIST = NoSpawnStructures.C_STRUCTURE_LIST;
	private static Boolean BLACKLIST = NoSpawnStructures.C_BLACKLIST;
	private static Integer LOGGING_LEVEL = NoSpawnStructures.C_LOGGING_LEVEL;

	@Inject(at=@At("HEAD"),method = "Lnet/minecraft/world/level/chunk/ChunkGenerator;tryGenerateStructure(Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;JLnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/core/SectionPos;)Z", cancellable = true)
	private void preventStructure(
		StructureSelectionEntry structureSelectionEntry,
		StructureManager structureManager,
		RegistryAccess registryAccess,
		RandomState randomState,
		StructureTemplateManager structureTemplateManager,
		long l,
		ChunkAccess chunkAccess,
		ChunkPos chunkPos,
		SectionPos sectionPos,
		CallbackInfoReturnable<Boolean> callback
	){
		double dist = 0;
		switch(SHAPE){
			case "Circle":
				dist = NoSpawnStructures.getDistance_Circle(chunkPos);
				break;
			case "Square":
				dist = NoSpawnStructures.getDistance_Square(chunkPos);
				break;
			case "Diamond":
				dist = NoSpawnStructures.getDistance_Diamond(chunkPos);
				break;
		}

		if(dist < RADIUS){
			String struct = structureSelectionEntry.structure().unwrapKey().get().location().toString();
			if(BLACKLIST){
				if(STRUCTURE_LIST.contains(struct)){
					if(LOGGING_LEVEL > 1){
						NoSpawnStructures.LOGGER.info("Blocked spawning at x" + Integer.toString(chunkPos.x) + " z" + Integer.toString(chunkPos.z) + " of structure \"" + struct + "\"");
					}
					callback.setReturnValue(false);
					return;
				}else{
					if(LOGGING_LEVEL > 2) {
						NoSpawnStructures.LOGGER.info("Was going to block spawning at x" + Integer.toString(chunkPos.x) + " z" + Integer.toString(chunkPos.z) + " of structure \"" + struct + "\" (not in blacklist)");
					}
				}
			}else{
				if(STRUCTURE_LIST.contains(struct)){
					if(LOGGING_LEVEL > 2){
						NoSpawnStructures.LOGGER.info("Was going to block spawning at x" + Integer.toString(chunkPos.x) + " z" + Integer.toString(chunkPos.z) + " of structure \"" + struct + "\" (is in whitelist)");
					}
				}else{
					if(LOGGING_LEVEL > 1){
						NoSpawnStructures.LOGGER.info("Blocked spawning at x" + Integer.toString(chunkPos.x) + " z" + Integer.toString(chunkPos.z) + " of structure \"" + struct + "\"");
					}
					callback.setReturnValue(false);
					return;
				}
			}
		}
	}
}