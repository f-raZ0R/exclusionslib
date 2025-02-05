package net.athebyne.exclusions_lib;
import net.athebyne.exclusions_lib.predicates.OverlapsStructureBlockPredicate;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.registry.Registries;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ExclusionsLib.MOD_ID)
public class ExclusionsLib {
    public static final String MOD_ID = "exclusions_lib";
    public static final Logger LOGGER = LoggerFactory.getLogger("Exclusions Lib");
    static final DeferredRegister<BlockPredicateType<?>> BLOCK_PREDICATE_TYPE = DeferredRegister.create(Registries.BLOCK_PREDICATE_TYPE.getKey(), MOD_ID);
    public static RegistryObject<BlockPredicateType<?>> OVERLAPS_STRUCTURE = BLOCK_PREDICATE_TYPE.register("overlaps_structure", () -> (BlockPredicateType) () -> OverlapsStructureBlockPredicate.CODEC);

    public ExclusionsLib(FMLJavaModLoadingContext context) {
        BLOCK_PREDICATE_TYPE.register(context.getModEventBus());
    }
}
