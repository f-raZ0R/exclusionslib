package net.athebyne.exclusions_lib.extensions;

import net.minecraft.util.Identifier;

public interface TagEntryExclusionHolder {
    Boolean exclusionsLib$isExcluded();
    void exclusionsLib$setExcluded(Boolean tagEntry);

    boolean exclusionsLib$isTag();
    boolean exclusionsLib$isRequired();
    Identifier exclusionsLib$getId();

}
