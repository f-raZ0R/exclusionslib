package net.athebyne.exclusions_lib;

import net.athebyne.exclusions_lib.predicates.OverlapsStructureBlockPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExclusionsLib {
    public static final String MOD_ID = "exclusions_lib";
    static final DeferredRegister<BlockPredicateType<?>> BLOCK_PREDICATE_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_PREDICATE_TYPE, MOD_ID);
    public static final DeferredHolder<BlockPredicateType<?>,BlockPredicateType<?>> OVERLAPS_STRUCTURE = BLOCK_PREDICATE_TYPES.register("overlaps_structure", () -> (BlockPredicateType) () -> OverlapsStructureBlockPredicate.CODEC);
    public static final Logger LOGGER = LoggerFactory.getLogger("Exclusions Lib");

    public ExclusionsLib(IEventBus modBus)
    {
        ExclusionsLib.BLOCK_PREDICATE_TYPES.register(modBus);
    }
}
