package net.athebyne.exclusions_lib.mixin;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.athebyne.exclusions_lib.ExclusionsLib;
import net.athebyne.exclusions_lib.extensions.TagEntryExclusionHolder;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Mixin(value = TagGroupLoader.class)
public class TagGroupLoaderMixin {

    @WrapOperation(
            method = "resolveAll(Lnet/minecraft/registry/tag/TagEntry$ValueGetter;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/tag/TagEntry;resolve(Lnet/minecraft/registry/tag/TagEntry$ValueGetter;Ljava/util/function/Consumer;)Z", ordinal = 0)
    )
    private <T> boolean exclusionsLib$removeEntry(TagEntry entry, TagEntry.ValueGetter<T> valueGetter,  Consumer<T> idConsumer, Operation<Boolean> original, TagEntry.ValueGetter<T> valueGetter1, List<TagGroupLoader.TrackedEntry> entries, @Local LocalRef<ImmutableSet.Builder<T>> builder)
    {
        if(((TagEntryExclusionHolder)entry).exclusionsLib$isExcluded())
        {
            ExclusionsLib.LOGGER.info("[Exclusions Lib] The Following Tag has been detected: " + entry);
            List<T> list = new java.util.ArrayList<>(builder.get().build().stream().toList());
            Identifier id = ((TagEntryExclusionHolder)entry).exclusionsLib$getId();
            boolean required = ((TagEntryExclusionHolder)entry).exclusionsLib$isRequired();
            if(((TagEntryExclusionHolder)entry).exclusionsLib$isTag())
            {
                Collection<T> collection = valueGetter.tag(id);
                if (collection == null) {
                    return !required;
                }
                list.removeAll(collection);
            }
            else
            {
                T object = valueGetter.direct(id);
                if (object == null) {
                    return !required;
                }
                list.removeAll(Collections.singleton(object));
            }
            builder.set(new ImmutableSet.Builder<T>().addAll(list));
            return true;
        }
        else
        {
            return original.call(entry,valueGetter,idConsumer);
        }
    }


}
