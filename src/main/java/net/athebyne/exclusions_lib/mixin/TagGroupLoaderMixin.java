package net.athebyne.exclusions_lib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import net.athebyne.exclusions_lib.ExclusionsLib;
import net.athebyne.exclusions_lib.extensions.TagEntryExclusionHolder;
import net.minecraft.registry.tag.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.List;
import java.util.SequencedSet;
import java.util.function.Consumer;

@Mixin(value = TagGroupLoader.class)
public class TagGroupLoaderMixin {

    @WrapOperation(
            method = "resolveAll(Lnet/minecraft/registry/tag/TagEntry$ValueGetter;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/tag/TagEntry;resolve(Lnet/minecraft/registry/tag/TagEntry$ValueGetter;Ljava/util/function/Consumer;)Z", ordinal = 0)
    )
    private <T> boolean exclusionsLib$removeEntry(TagEntry entry, TagEntry.ValueGetter<T> valueGetter, Consumer<T> idConsumer, Operation<Boolean> original, TagEntry.ValueGetter<T> valueGetter1, List<TagGroupLoader.TrackedEntry> entries, @Local SequencedSet<T> set)
    {
        TagEntryExclusionHolder holder = (TagEntryExclusionHolder) entry;
        if (!holder.exclusionsLib$isExcluded()) //noinspection MixinExtrasOperationParameters
	        return original.call(entry, valueGetter, idConsumer);

	    ExclusionsLib.LOGGER.info("[Exclusions Lib] The Following Tag has been detected: {}", entry);
        Identifier id = holder.exclusionsLib$getId();
        boolean required = holder.exclusionsLib$isRequired();
        if (holder.exclusionsLib$isTag())
        {
            Collection<T> collection = valueGetter.tag(id);
            if (collection == null) return !required;
            set.removeAll(collection);
        }
        else
        {
            T object = valueGetter.direct(id, required);
            if (object == null) return !required;
            set.remove(object);
        }
        return true;
    }

}
