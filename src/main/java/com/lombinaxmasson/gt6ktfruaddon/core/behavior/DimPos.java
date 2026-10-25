package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import net.minecraft.core.BlockPos;

/** A block position in one dimension: the key the addon-side machine indexes use. */
public record DimPos(String dim, BlockPos pos) {
    public DimPos {
        java.util.Objects.requireNonNull(dim);
        pos = java.util.Objects.requireNonNull(pos).immutable();
    }
}
