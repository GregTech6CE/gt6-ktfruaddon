package com.lombinaxmasson.ktfruaddon;

import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMaterialContributor;
import com.gregtech.gregtech.api.addon.MaterialDeclaration;
import com.gregtech.gregtech.api.addon.MaterialFormRequest;

/** One dust. Purity grades that would need a new prefix are recorded in GAP.md. */
public final class KtfruMaterials implements AddonMaterialContributor {
    @Override
    public String addonId() {
        return KtfruAddon.MODID;
    }

    @Override
    public List<MaterialDeclaration> materials() {
        return List.of(MaterialDeclaration.builder("MonocrystallineSilicon")
                .displayName("Monocrystalline Silicon")
                .color(0xD0E8FF)
                .chemicalFormula("Si")
                .meltingPointKelvin(1687)
                .boilingPointKelvin(3538)
                .density(2.33F)
                .dust()
                .build());
    }

    @Override
    public List<MaterialFormRequest> forms() {
        return List.of();
    }
}
