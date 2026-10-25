package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

/** Cached links. Reads x,y,z, semicolon lists, and dimension-prefixed pipe lists; writes v1. */
public final class LinkList {
    private String raw;
    private String dimension;
    private List<DimPos> cached = List.of();

    public List<DimPos> read(String text, String defaultDimension) {
        if (Objects.equals(raw, text) && Objects.equals(dimension, defaultDimension)) return cached;
        List<DimPos> found = new ArrayList<>();
        if (text != null && !text.isEmpty()) {
            for (String piece : text.split("[;|]")) {
                if (piece.equals("v1")) continue;
                int z = piece.lastIndexOf(',');
                int y = piece.lastIndexOf(',', z - 1);
                int x = piece.lastIndexOf(',', y - 1);
                String dim = x < 0 ? defaultDimension : piece.substring(0, x);
                String xyz = x < 0 ? piece : piece.substring(x + 1);
                BlockPos pos = PosCodec.parse(xyz);
                if (pos != null && dim != null && ResourceLocation.tryParse(dim) != null) {
                    found.add(new DimPos(dim, pos));
                }
            }
        }
        raw = text;
        dimension = defaultDimension;
        cached = List.copyOf(found);
        return cached;
    }

    public static String write(List<DimPos> links) {
        if (links.isEmpty()) return "";
        StringBuilder result = new StringBuilder("v1");
        for (DimPos link : links) result.append('|').append(link.dim()).append(',').append(PosCodec.format(link.pos()));
        return result.toString();
    }
}
