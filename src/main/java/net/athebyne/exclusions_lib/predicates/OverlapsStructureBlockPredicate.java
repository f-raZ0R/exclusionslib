package net.athebyne.exclusions_lib.predicates;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.athebyne.exclusions_lib.ExclusionsLib;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.*;
import net.minecraft.util.math.*;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.blockpredicate.*;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OverlapsStructureBlockPredicate implements BlockPredicate {

    public static final MapCodec<OverlapsStructureBlockPredicate> CODEC = RecordCodecBuilder.mapCodec(
            (instance) ->
                    instance.group(
                            Vec3i.createOffsetCodec(16).optionalFieldOf("offset", BlockPos.ORIGIN).forGetter((predicate) -> predicate.offset),
                            RegistryCodecs.entryList(RegistryKeys.STRUCTURE).optionalFieldOf("structures").forGetter(OverlapsStructureBlockPredicate::structure),
                            Codec.intRange(0, 32).optionalFieldOf("range", 0).forGetter((predicate) -> predicate.range)
                    ).apply(instance, OverlapsStructureBlockPredicate::new)
    );

    private final Vec3i offset;
    private final int range;
    private final List<Structure> structures;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<RegistryEntryList<Structure>> rawStructures;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public OverlapsStructureBlockPredicate(Vec3i offset, Optional<RegistryEntryList<Structure>> structures, int range) {
        this.rawStructures = structures;
        this.structures = structures
                .map(s -> s.stream().map(RegistryEntry::value).collect(Collectors.toList()))
                .orElse(null);
        this.offset = offset;
        this.range = range;

    }
    
    public boolean test(StructureWorldAccess structureWorldAccess, BlockPos blockPos) {
        ServerWorld world = structureWorldAccess.toServerWorld();
        StructureAccessor accessor = world.getStructureAccessor();
        BlockPos blockPosOffset = blockPos.add(this.offset);
        Predicate<StructureStart> predicate = makePredicate(blockPosOffset);
        for (var struct : accessor.getStructureReferences(blockPosOffset).entrySet()) {
            if (this.structures != null && !this.structures.contains(struct.getKey())) continue;
            for (long pos : struct.getValue()) {
                ChunkSectionPos sectionPos = ChunkSectionPos.from(new ChunkPos(pos), world.getBottomSectionCoord());
                StructureStart start = accessor.getStructureStart(
                        sectionPos, struct.getKey(), world.getChunk(sectionPos.getSectionX(), sectionPos.getSectionZ(), ChunkStatus.STRUCTURE_STARTS)
                );
                if (start != null && start.hasChildren() && predicate.test(start)) return true;
            }
        }
        return false;
    }

    private @NotNull Predicate<StructureStart> makePredicate(BlockPos blockPosOffset) {
        final BlockBox exclusionZone = new BlockBox(
                blockPosOffset.getX() - this.range, blockPosOffset.getY() - this.range, blockPosOffset.getZ() - this.range,
                blockPosOffset.getX() + this.range, blockPosOffset.getY() + this.range, blockPosOffset.getZ() + this.range
        );
	    return start -> {
	        for (StructurePiece piece : start.getChildren())
	            if (piece.getBoundingBox().intersects(exclusionZone)) return true;
	        return false;
	    };
    }

    public Optional<RegistryEntryList<Structure>> structure() {
        return rawStructures;
    }

    public BlockPredicateType<?> getType() {
        return ExclusionsLib.OVERLAPS_STRUCTURE;
    }

}
