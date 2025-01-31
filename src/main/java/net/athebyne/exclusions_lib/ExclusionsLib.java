package net.athebyne.exclusions_lib;

import net.athebyne.exclusions_lib.predicates.OverlapsStructureBlockPredicate;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExclusionsLib implements ModInitializer {
    public static final String MOD_ID = "exclusions_lib";
    public static BlockPredicateType<OverlapsStructureBlockPredicate> OVERLAPS_STRUCTURE;
    public static final Logger LOGGER = LoggerFactory.getLogger("Exclusions Lib");

    @Override
    public void onInitialize() {
        OVERLAPS_STRUCTURE = Registry.register(Registries.BLOCK_PREDICATE_TYPE, Identifier.of(MOD_ID, "overlaps_structure"), () -> OverlapsStructureBlockPredicate.CODEC);
    }
}
