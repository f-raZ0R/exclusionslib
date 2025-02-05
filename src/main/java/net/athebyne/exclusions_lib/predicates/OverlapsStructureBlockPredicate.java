package net.athebyne.exclusions_lib.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.*;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.world.gen.structure.Structure;
import net.athebyne.exclusions_lib.*;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class OverlapsStructureBlockPredicate implements BlockPredicate {

    public static final Codec<OverlapsStructureBlockPredicate> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Vec3i.createOffsetCodec(16).optionalFieldOf("offset", BlockPos.ORIGIN).forGetter((predicate) -> predicate.offset),
            RegistryCodecs.entryList(RegistryKeys.STRUCTURE).optionalFieldOf("structures").forGetter(OverlapsStructureBlockPredicate::structure),
            Codec.intRange(0, 32).optionalFieldOf("range", 0).forGetter((predicate) -> predicate.range)).apply(instance, OverlapsStructureBlockPredicate::new));

    private final Vec3i offset;
    private final int range;
    private final Optional<RegistryEntryList<Structure>> rawStructures;
    private final Optional<List<Structure>> structures;
    public OverlapsStructureBlockPredicate(Vec3i offset,  Optional<RegistryEntryList<Structure>> structures, int range) {
        this.rawStructures = structures;
        if (structures.isEmpty()) {
            this.structures = Optional.empty();
        } else {
            this.structures = Optional.of(structures.get().stream().map(RegistryEntry::value).collect(Collectors.toList()));
        }
        this.offset = offset;
        this.range = range;

    }
    public boolean test(StructureWorldAccess structureWorldAccess, BlockPos blockPos) {
        StructureAccessor accessor = structureWorldAccess.toServerWorld().getStructureAccessor();
        BlockPos blockPosOffset = blockPos.add(this.offset);
        BlockBox exclusionZone = new BlockBox(blockPosOffset.getX() - this.range, blockPosOffset.getY() - this.range, blockPosOffset.getZ() - this.range,blockPosOffset.getX() + this.range, blockPosOffset.getY() + this.range, blockPosOffset.getZ() + this.range);
        for(var struct : accessor.getStructureReferences(blockPosOffset).entrySet())
        {
            if(this.structures.isPresent() && !this.structures.get().contains(struct.getKey()))
            {
                continue;
            }
            Predicate<StructureStart> predicate = start -> {
                for (StructurePiece piece : start.getChildren()) {
                    if (piece.getBoundingBox().intersects(exclusionZone)) {
                        return true;
                    }
                }
                return false;
            };

            MutableBoolean overlappingBox = new MutableBoolean(false);
            accessor.acceptStructureStarts(struct.getKey(), struct.getValue(), (start) -> {
                if (overlappingBox.isFalse() && predicate.test(start)) {
                    overlappingBox.setTrue();
                }
            });

            if (overlappingBox.isTrue()) {
                return true;
            }
        }
        return false;
    }

    public Optional<RegistryEntryList<Structure>> structure() {
        return rawStructures;
    }

    public BlockPredicateType<?> getType() {
        return ExclusionsLib.OVERLAPS_STRUCTURE.get();
    }

}
