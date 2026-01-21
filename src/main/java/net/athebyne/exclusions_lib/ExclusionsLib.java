package net.athebyne.exclusions_lib;

import net.athebyne.exclusions_lib.predicates.OverlapsStructureBlockPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ExclusionsLib.MOD_ID)
public class ExclusionsLib {
    public static final String MOD_ID = "exclusions_lib";
    static final DeferredRegister<BlockPredicateType<?>> BLOCK_PREDICATE_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_PREDICATE_TYPE, MOD_ID);
    public static final DeferredHolder<BlockPredicateType<?>,BlockPredicateType<?>> OVERLAPS_STRUCTURE = BLOCK_PREDICATE_TYPES.register("overlaps_structure", () -> (BlockPredicateType) () -> OverlapsStructureBlockPredicate.CODEC);
    public static final Logger LOGGER = LogManager.getLogger("Exclusions Lib");


    public ExclusionsLib(IEventBus modBus)
    {
        ExclusionsLib.BLOCK_PREDICATE_TYPES.register(modBus);
    }
}
