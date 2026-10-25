package com.lombinaxmasson.gt6ktfruaddon.content;

import java.util.List;
import java.util.Set;

import com.gregtech.gregtech.api.addon.AddonMaterialContributor;
import com.gregtech.gregtech.api.addon.MaterialDeclaration;
import com.gregtech.gregtech.api.addon.MaterialFormRequest;
import com.gregtech.gregtech.api.addon.PrefixCondition;
import com.gregtech.gregtech.api.addon.PrefixDeclaration;
import com.lombinaxmasson.gt6ktfruaddon.Ktfru;

/**
 * Chemical materials from kTFRUAddon materialPreInit at 6404ab69. Bittern is the
 * one extra liquid: it is an flList fluid the tiny distillation recipe outputs.
 * Temperatures follow the source: Celsius entries are stored as Celsius plus 273. Density is 1
 * because that list does not carry one. Plate, foil and ore requests stay empty:
 * a form the material flags do not allow fails startup, and ore is not a generated prefix.
 */
public final class Materials implements AddonMaterialContributor {
    @Override public String addonId() { return Ktfru.MODID; }

    private static List<MaterialDeclaration> declared;

    /** Built on the first call and kept: the creative tab asks every time it fills. */
    @Override public List<MaterialDeclaration> materials() {
        synchronized (Materials.class) {
            if (declared == null) declared = declareMaterials();
            return declared;
        }
    }

    private static List<MaterialDeclaration> declareMaterials() {
        return List.of(
                MaterialDeclaration.builder("AmmoniumDichromate").displayName("Ammonium Dichromate").color(0xFFA033).chemicalFormula("Cr₂O₇(NH3)₂").meltingPointKelvin(456).boilingPointKelvin(460).density(1F).dust().build(),
                MaterialDeclaration.builder("AmmoniumChromicSulfate").displayName("Ammonium Chromic Sulfate").color(0x4800A1).chemicalFormula("NH₄Cr(SO₄)₂").meltingPointKelvin(456).boilingPointKelvin(460).density(1F).dust().build(),
                MaterialDeclaration.builder("AmmoniumIronIIISulfate").displayName("Ammonium Iron(III) Sulfate").color(0xE6DCF2).chemicalFormula("NH₄Fe(SO₄)₂").meltingPointKelvin(368).boilingPointKelvin(524).density(1F).dust().build(),
                MaterialDeclaration.builder("AmmoniumSulfate").displayName("Ammonium Sulfate").color(0xFBFBD8).chemicalFormula("(NH₄)₂SO₄").meltingPointKelvin(509).boilingPointKelvin(510).density(1F).dust().build(),
                MaterialDeclaration.builder("CookedBauxide").displayName("Cooked Bauxide").color(0xE58D00).chemicalFormula("").meltingPointKelvin(2370).boilingPointKelvin(3234).density(1F).dust().build(),
                MaterialDeclaration.builder("AcidPickledBauxide").displayName("Acid Pickled Bauxide").color(0xC8A800).chemicalFormula("").meltingPointKelvin(2370).boilingPointKelvin(3234).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("BauxiteRedMud").displayName("Bauxite Red Mud").color(0x94000C).chemicalFormula("").meltingPointKelvin(1790).boilingPointKelvin(3234).density(1F).dust().build(),
                MaterialDeclaration.builder("LithiumCarbonate").displayName("Lithium Carbonate").color(0xF8F4F8).chemicalFormula("Li₂CO₃").meltingPointKelvin(943).boilingPointKelvin(1582).density(1F).dust().build(),
                MaterialDeclaration.builder("MetatitanicAcid").displayName("Metatitanic Acid").color(0xF8F4F8).chemicalFormula("TiO(OH)₂").meltingPointKelvin(748).boilingPointKelvin(749).density(1F).dust().build(),
                MaterialDeclaration.builder("OilScarp").displayName("Oil Scarp").color(0x0C0902).chemicalFormula("").meltingPointKelvin(363).boilingPointKelvin(504).density(1F).dust().build(),
                MaterialDeclaration.builder("Methanol").displayName("Methanol").color(0x5A50D2).chemicalFormula("CH₃OH").meltingPointKelvin(175).boilingPointKelvin(337).density(1F).liquid().build(),
                MaterialDeclaration.builder("Propanol").displayName("Propanol").color(0x3C3C96).chemicalFormula("CH₂CH₂CH₃OH").meltingPointKelvin(146).boilingPointKelvin(370).density(1F).liquid().build(),
                // Source materialPreInit.java:49 repeats Propanol's formula; preserved pending upstream correction (D9).
                MaterialDeclaration.builder("Propanediol").displayName("Propanediol").color(0x3C3C96).chemicalFormula("CH₂CH₂CH₃OH").meltingPointKelvin(213).boilingPointKelvin(460).density(1F).liquid().build(),
                MaterialDeclaration.builder("Ethane").displayName("Ethane").color(0x3C3C96).chemicalFormula("CH₃CH₃").meltingPointKelvin(101).boilingPointKelvin(185).density(1F).liquid().build(),
                MaterialDeclaration.builder("Acetone").displayName("Acetone").color(0xD2D2D2).chemicalFormula("C₃H₆O").meltingPointKelvin(143).boilingPointKelvin(293).density(1F).liquid().build(),
                MaterialDeclaration.builder("Acetylene").displayName("Acetylene").color(0xD2D2D2).chemicalFormula("CH≡CH").meltingPointKelvin(147).boilingPointKelvin(217).density(1F).gas().build(),
                MaterialDeclaration.builder("Benzene").displayName("Benzene").color(0xFFFF82).chemicalFormula("C₆H₆").meltingPointKelvin(278).boilingPointKelvin(353).density(1F).liquid().build(),
                MaterialDeclaration.builder("Toluene").displayName("Toluene").color(0xFFFF82).chemicalFormula("CH₃C₅H₆").meltingPointKelvin(178).boilingPointKelvin(383).density(1F).liquid().build(),
                MaterialDeclaration.builder("Formaldehyde").displayName("Formaldehyde").color(0xFFFFFF).chemicalFormula("CH₂O").meltingPointKelvin(258).boilingPointKelvin(370).density(1F).gas().build(),
                MaterialDeclaration.builder("Acetaldehyde").displayName("Acetaldehyde").color(0xFFFFFF).chemicalFormula("O=CHCH₃").meltingPointKelvin(148).boilingPointKelvin(294).density(1F).gas().build(),
                MaterialDeclaration.builder("Propionaldehyde").displayName("Propionaldehyde").color(0xFFFFFF).chemicalFormula("O=CHCH₂CH₃").meltingPointKelvin(192).boilingPointKelvin(319).density(1F).gas().build(),
                MaterialDeclaration.builder("HypochlorousAcid").displayName("Hypochlorous Acid").color(0xB4FFB4).chemicalFormula("HClO").meltingPointKelvin(273).boilingPointKelvin(373).density(1F).liquid().build(),
                MaterialDeclaration.builder("ChloricAcid").displayName("Chloric Acid").color(0xEBFFD2).chemicalFormula("HClO₃").meltingPointKelvin(273).boilingPointKelvin(314).density(1F).liquid().build(),
                MaterialDeclaration.builder("PerchloricAcid").displayName("Perchloric Acid").color(0xFFFFFF).chemicalFormula("HClO₄").meltingPointKelvin(161).boilingPointKelvin(292).density(1F).liquid().build(),
                MaterialDeclaration.builder("Phosgene").displayName("Phosgene").color(0xF0F5FF).chemicalFormula("COCl₂").meltingPointKelvin(145).boilingPointKelvin(281).density(1F).gas().build(),
                MaterialDeclaration.builder("AllylChloride").displayName("AllylChloride").color(0xD6E7DE).chemicalFormula("CH₂=CHCH₂Cl").meltingPointKelvin(137).boilingPointKelvin(317).density(1F).liquid().build(),
                MaterialDeclaration.builder("DichloroPropanol").displayName("DichloroPropanol").color(0xF5F5F5).chemicalFormula("CHOH(CH₂Cl)₂").meltingPointKelvin(269).boilingPointKelvin(446).density(1F).liquid().build(),
                MaterialDeclaration.builder("Epichlorohydrin").displayName("Epichlorohydrin").color(0xF5F5F5).chemicalFormula("C₃H₅ClO").meltingPointKelvin(216).boilingPointKelvin(388).density(1F).liquid().build(),
                MaterialDeclaration.builder("TriethylAluminium").displayName("Triethyl Aluminium").color(0xFFFFFF).chemicalFormula("Al(CH₂CH₃)₃").meltingPointKelvin(223).boilingPointKelvin(401).density(1F).dust().build(),
                MaterialDeclaration.builder("CalciumCarbide").displayName("CalciumCarbide").color(0xFFFFFF).chemicalFormula("CaC₂").meltingPointKelvin(720).boilingPointKelvin(2573).density(1F).dust().build(),
                MaterialDeclaration.builder("CalciumHydroxide").displayName("CalciumHydroxide").color(0xFFFFFF).chemicalFormula("Ca(OH)₂").meltingPointKelvin(853).boilingPointKelvin(3123).density(1F).dust().build(),
                MaterialDeclaration.builder("Chloromethane").displayName("Chloromethane").color(0xFFFFFF).chemicalFormula("CH₃Cl").meltingPointKelvin(176).boilingPointKelvin(249).density(1F).liquid().build(),
                MaterialDeclaration.builder("Dichloromethane").displayName("Dichloromethane").color(0xFFFFFF).chemicalFormula("CH₂Cl₂").meltingPointKelvin(176).boilingPointKelvin(312).density(1F).liquid().build(),
                MaterialDeclaration.builder("Chloroform").displayName("Chloroform").color(0xD5D4D4).chemicalFormula("CHCl₃").meltingPointKelvin(210).boilingPointKelvin(334).density(1F).liquid().build(),
                MaterialDeclaration.builder("CalciumAcetate").displayName("Calcium Acetate").color(0xF5F6F5).chemicalFormula("(CH₃COO)₂Ca").meltingPointKelvin(668).boilingPointKelvin(670).density(1F).dust().build(),
                MaterialDeclaration.builder("ZincChromate").displayName("Zinc Chromate").color(0xD6C600).chemicalFormula("ZnCrO₄").meltingPointKelvin(589).boilingPointKelvin(924).density(1F).dust().build(),
                MaterialDeclaration.builder("ZincOxide").displayName("ZincOxide").color(0xD6E7DE).chemicalFormula("ZnO").meltingPointKelvin(2248).boilingPointKelvin(2633).density(1F).dust().build(),
                MaterialDeclaration.builder("ZincChloride").displayName("ZincChloride").color(0xD6E7DE).chemicalFormula("ZnCl₂").meltingPointKelvin(566).boilingPointKelvin(1005).density(1F).dust().build(),
                MaterialDeclaration.builder("BPA").displayName("BPA").color(0xFFFFFF).chemicalFormula("CH₃C(PhOH)₂CH₃").meltingPointKelvin(431).boilingPointKelvin(499).density(1F).dust().build(),
                MaterialDeclaration.builder("DiphenylCarbonate").displayName("Diphenyl Carbonate").color(0xFFFFFF).chemicalFormula("PhOCOOPh").meltingPointKelvin(355).boilingPointKelvin(574).density(1F).dust().build(),
                MaterialDeclaration.builder("Tetrafluoroethylene").displayName("Tetrafluoroethylene").color(0xFFFFFF).chemicalFormula("CF₂CF₂").meltingPointKelvin(131).boilingPointKelvin(197).density(1F).gas().build(),
                // Source :138-139 requests INGOT_BASED, dust, plates and foils; metal() enables those forms (D9).
                MaterialDeclaration.builder("EpoxyResin").displayName("Epoxy Resin").color(0xFFFFFF).chemicalFormula("(C₃H₅ClO)").meltingPointKelvin(388).boilingPointKelvin(525).density(1F).metal().build(),
                MaterialDeclaration.builder("VinylAcetate").displayName("VinylAcetate").color(0xFFFFFF).chemicalFormula("CH₃CHOOCH=CH₂").meltingPointKelvin(180).boilingPointKelvin(345).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("PolyVinylAcetate").displayName("PolyVinylAcetate").color(0xFFFFFF).chemicalFormula("(C₄H₆O₂)").meltingPointKelvin(333).boilingPointKelvin(416).density(1F).dust().build(),
                // Source materialPreInit.java:147 spells the last group CH2 + subscript 2; preserved (D9).
                MaterialDeclaration.builder("Butadiene").displayName("Butadiene").color(0xFFFFFF).chemicalFormula("CH₂=CHCH=CH2₂").meltingPointKelvin(164).boilingPointKelvin(269).density(1F).gas().build(),
                MaterialDeclaration.builder("MethylmagnesiumChloride").displayName("MethylmagnesiumChloride").color(0xFFFFFF).chemicalFormula("CH₃ClMg").meltingPointKelvin(243).boilingPointKelvin(339).density(1F).liquid().build(),
                MaterialDeclaration.builder("Methyltrichlorosilane").displayName("Methyltrichlorosilane").color(0xFFFFFF).chemicalFormula("CH₃Cl₃Si").meltingPointKelvin(196).boilingPointKelvin(339).density(1F).liquid().build(),
                MaterialDeclaration.builder("Dichlorodimethylsilane").displayName("Dichlorodimethylsilane").color(0xFFFFFF).chemicalFormula("(CH₃)₂Cl₂Si").meltingPointKelvin(197).boilingPointKelvin(333).density(1F).liquid().build(),
                MaterialDeclaration.builder("SiliconeRubber").displayName("SiliconeRubber").color(0xFFFFFF).chemicalFormula("").meltingPointKelvin(371).boilingPointKelvin(510).density(1F).dust().liquid().polymer().build(),
                MaterialDeclaration.builder("MercuryIIChloride").displayName("MercuryIIChloride").color(0xFFFFFF).chemicalFormula("Cl₂Hg₂").meltingPointKelvin(550).boilingPointKelvin(575).density(1F).dust().build(),
                MaterialDeclaration.builder("DichloroEthane").displayName("DichloroEthane").color(0xFFFFFF).chemicalFormula("CH₂ClCH₂Cl").meltingPointKelvin(238).boilingPointKelvin(355).density(1F).liquid().build(),
                MaterialDeclaration.builder("DichloroPropane").displayName("DichloroPropane").color(0xFFFFFF).chemicalFormula("CH₃CH₂Cl=CH₂Cl").meltingPointKelvin(173).boilingPointKelvin(368).density(1F).liquid().build(),
                MaterialDeclaration.builder("VinylChloride").displayName("VinylChloride").color(0xFFFFFF).chemicalFormula("CH₂=CHCl").meltingPointKelvin(120).boilingPointKelvin(260).density(1F).liquid().build(),
                MaterialDeclaration.builder("Ethylbenzene").displayName("Ethylbenzene").color(0xFFFFFF).chemicalFormula("PhCH₂CH₃").meltingPointKelvin(178).boilingPointKelvin(409).density(1F).liquid().build(),
                MaterialDeclaration.builder("ChloroPhenylethane").displayName("ChloroPhenylethane").color(0xFFFFFF).chemicalFormula("PhCHClCH₃").meltingPointKelvin(231).boilingPointKelvin(363).density(1F).liquid().build(),
                MaterialDeclaration.builder("Styrene").displayName("Styrene").color(0xFFFFFF).chemicalFormula("PhCH=CH₂").meltingPointKelvin(242).boilingPointKelvin(418).density(1F).liquid().build(),
                MaterialDeclaration.builder("SBR").displayName("SBR").color(0x191520).chemicalFormula("").meltingPointKelvin(332).boilingPointKelvin(663).density(1F).dust().liquid().polymer().build(),
                MaterialDeclaration.builder("Isoprene").displayName("Isoprene").color(0xFFFFFF).chemicalFormula("CH₂=CCH₃CHCH₂").meltingPointKelvin(128).boilingPointKelvin(305).density(1F).liquid().build(),
                MaterialDeclaration.builder("Chloroethanol").displayName("Chloroethanol").color(0xFFFFFF).chemicalFormula("C₂H₅ClO").meltingPointKelvin(206).boilingPointKelvin(402).density(1F).liquid().build(),
                MaterialDeclaration.builder("EthyleneGlycol").displayName("EthyleneGlycol").color(0xFFFFFF).chemicalFormula("(CH₂OH)₂").meltingPointKelvin(260).boilingPointKelvin(470).density(1F).liquid().build(),
                MaterialDeclaration.builder("PolymethylMethacrylate").displayName("PolymethylMethacrylate").color(0xFFFFFF).chemicalFormula("(C₅O₂H₈)").meltingPointKelvin(423).boilingPointKelvin(507).density(1F).dust().build(),
                MaterialDeclaration.builder("PVA").displayName("PVA").color(0xFFFFFF).chemicalFormula("(CH₂CHOH)").meltingPointKelvin(405).boilingPointKelvin(406).density(1F).dust().build(),
                MaterialDeclaration.builder("PrecursorPSVE").displayName("PrecursorPSVE").color(0xFFFFFF).chemicalFormula("").meltingPointKelvin(234).boilingPointKelvin(410).density(1F).liquid().build(),
                MaterialDeclaration.builder("HPF").displayName("HPF").color(0xFFFFFF).chemicalFormula("CF₂=CFCF₃").meltingPointKelvin(120).boilingPointKelvin(245).density(1F).gas().build(),
                MaterialDeclaration.builder("TFES").displayName("TFES").color(0xFFFFFF).chemicalFormula("C₂F₄O₃S").meltingPointKelvin(238).boilingPointKelvin(314).density(1F).liquid().build(),
                MaterialDeclaration.builder("HFPO").displayName("HFPO").color(0xFFFFFF).chemicalFormula("C₃F₆O").meltingPointKelvin(144).boilingPointKelvin(231).density(1F).gas().build(),
                MaterialDeclaration.builder("PSVE").displayName("PSVE").color(0xFFFFFF).chemicalFormula("C₇F₁₄O₄S").meltingPointKelvin(231).boilingPointKelvin(408).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("Tetrachorosilane").displayName("Tetrachorosilane").color(0xFFFFFF).chemicalFormula("SiCl₄").meltingPointKelvin(203).boilingPointKelvin(330).density(1F).liquid().build(),
                MaterialDeclaration.builder("PFSA").displayName("PFSA").color(0xFFFFFF).chemicalFormula("").meltingPointKelvin(387).boilingPointKelvin(740).density(1F).dust().build(),
                MaterialDeclaration.builder("perfluorocyclobutane").displayName("perfluorocyclobutane").color(0xFFFFFF).chemicalFormula("C₄F₈").meltingPointKelvin(232).boilingPointKelvin(265).density(1F).gas().build(),
                MaterialDeclaration.builder("HydrobromicAcid").displayName("HydrobromicAcid").color(0xFFFFFF).chemicalFormula("HBr").meltingPointKelvin(176).boilingPointKelvin(206).density(1F).gas().build(),
                MaterialDeclaration.builder("Naphthalene").displayName("Naphthalene").color(0xFFFFFF).chemicalFormula("C₁₀H₈").meltingPointKelvin(354).boilingPointKelvin(491).density(1F).dust().build(),
                MaterialDeclaration.builder("Naphthalenol").displayName("1-naphthalenol").color(0xFFFFFF).chemicalFormula("C₁₀H₈O").meltingPointKelvin(369).boilingPointKelvin(551).density(1F).dust().build(),
                MaterialDeclaration.builder("NaphthalenolAminoHydrochloride").displayName("1-Naphthalenol-2-amino-hydrochloride").color(0xFFFFFF).chemicalFormula("C₁₀H₁₀ClNO").meltingPointKelvin(416).boilingPointKelvin(616).density(1F).dust().build(),
                MaterialDeclaration.builder("DiazoNaphthol").displayName("DiazoNaphthol").color(0xFFFFFF).chemicalFormula("C₁₀H₅N₂O").meltingPointKelvin(416).boilingPointKelvin(616).density(1F).dust().build(),
                MaterialDeclaration.builder("MethoxyPropanol").displayName("MethoxyPropanol").color(0xFFFFFF).chemicalFormula("C₄H₁₀O₂").meltingPointKelvin(176).boilingPointKelvin(393).density(1F).liquid().build(),
                MaterialDeclaration.builder("PGMEA").displayName("PGMEA").color(0xFFFFFF).chemicalFormula("C₆H₁₂O₃").meltingPointKelvin(186).boilingPointKelvin(418).density(1F).liquid().build(),
                MaterialDeclaration.builder("DNQPhotoresist").displayName("DNQPhotoresist").color(0xFFFFFF).chemicalFormula("").meltingPointKelvin(486).boilingPointKelvin(614).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("Cyclohexanol").displayName("Cyclohexanol").color(0xFFFFFF).chemicalFormula("C₆H₁₂O").meltingPointKelvin(293).boilingPointKelvin(433).density(1F).dust().build(),
                MaterialDeclaration.builder("Cyclohexanone").displayName("Cyclohexanone").color(0xFFFFFF).chemicalFormula("C₆H₁₀O").meltingPointKelvin(226).boilingPointKelvin(388).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("Acenaphthylene").displayName("Acenaphthylene").color(0xFFFFFF).chemicalFormula("C₁₂H₈").meltingPointKelvin(351).boilingPointKelvin(553).density(1F).dust().build(),
                MaterialDeclaration.builder("Nitroacenaphthene").displayName("5-Nitroacenaphthene").color(0xFFFFFF).chemicalFormula("C₁₂H₉NO₂").meltingPointKelvin(373).boilingPointKelvin(552).density(1F).dust().build(),
                MaterialDeclaration.builder("PMMAPhotoresist").displayName("PMMAPhotoresist").color(0xFFFFFF).chemicalFormula("").meltingPointKelvin(501).boilingPointKelvin(594).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("AmmoniumNitrate").displayName("AmmoniumNitrate").color(0xFFFFFF).chemicalFormula("NH4NO₃").meltingPointKelvin(442).boilingPointKelvin(483).density(1F).dust().build(),
                MaterialDeclaration.builder("PotassiumSodium").displayName("Potassium Sodium").color(0xB4E640).chemicalFormula("NaK₃").meltingPointKelvin(280).boilingPointKelvin(1086).density(1F).metal().liquid().build(),
                MaterialDeclaration.builder("HotPotassiumSodium").displayName("Hot Potassium Sodium").color(0xB4E640).chemicalFormula("NaK₃").meltingPointKelvin(800).boilingPointKelvin(1086).density(1F).liquid().build(),
                MaterialDeclaration.builder("UraniumNitrateHexahydrate").displayName("UraniumNitrateHexahydrate").color(0x32F032).chemicalFormula("H₄N₂O₉U").meltingPointKelvin(330).boilingPointKelvin(365).density(1F).dust().build(),
                MaterialDeclaration.builder("TributylPhosphate").displayName("TributylPhosphate").color(0xFFFFFF).chemicalFormula("C₁₂H₂₇O₄P").meltingPointKelvin(194).boilingPointKelvin(561).density(1F).liquid().build(),
                MaterialDeclaration.builder("Butanol").displayName("Butanol").color(0xFFFFFF).chemicalFormula("CH₃CH₂CH₂CH₂OH").meltingPointKelvin(183).boilingPointKelvin(389).density(1F).liquid().build(),
                MaterialDeclaration.builder("UranylCarbonateSolution").displayName("UranylCarbonateSolution").color(0xFFFFFF).chemicalFormula("").meltingPointKelvin(183).boilingPointKelvin(395).density(1F).liquid().build(),
                MaterialDeclaration.builder("MethylTertiaryAmine").displayName("MethylTertiaryAmine").color(0xFFFFFF).chemicalFormula("N(CH₃)₃").meltingPointKelvin(203).boilingPointKelvin(423).density(1F).liquid().build(),
                MaterialDeclaration.builder("EthylOleate").displayName("EthylOleate").color(0xFFFFFF).chemicalFormula("C₂₀H₃₈O₂").meltingPointKelvin(241).boilingPointKelvin(489).density(1F).liquid().build(),
                MaterialDeclaration.builder("OleicAcid").displayName("OleicAcid").color(0xFFFFFF).chemicalFormula("C₁₈H₃₄O₂").meltingPointKelvin(286).boilingPointKelvin(633).density(1F).dust().build(),
                MaterialDeclaration.builder("UraniniteCinder").displayName("Uraninite Cinder").color(0x7BC81E).chemicalFormula("").meltingPointKelvin(1778).boilingPointKelvin(4144).density(1F).dust().build(),
                MaterialDeclaration.builder("PitchblendeCinder").displayName("Pitchblende Cinder").color(0x91F032).chemicalFormula("").meltingPointKelvin(1778).boilingPointKelvin(4144).density(1F).dust().build(),
                MaterialDeclaration.builder("Silane").displayName("Silane").color(0xFFFFFF).chemicalFormula("H₄Si").meltingPointKelvin(88).boilingPointKelvin(161).density(1F).gas().build(),
                MaterialDeclaration.builder("MagnesiumSilicide").displayName("Magnesium Silicide").color(0xFFFFFF).chemicalFormula("Mg₂Si").meltingPointKelvin(1375).boilingPointKelvin(1473).density(1F).dust().build(),
                MaterialDeclaration.builder("AmmoniumChloride").displayName("Ammonium Chloride").color(0xFFFFFF).chemicalFormula("NH₄Cl").meltingPointKelvin(613).boilingPointKelvin(926).density(1F).dust().build(),
                MaterialDeclaration.builder("SiliconTetrafluoride").displayName("Silicon Tetrafluoride").color(0xFFFFFF).chemicalFormula("SiF₄").meltingPointKelvin(183).boilingPointKelvin(187).density(1F).gas().build(),
                MaterialDeclaration.builder("SodiumAluminiumHydride").displayName("Sodium Aluminium Hydride").color(0xFFFFFF).chemicalFormula("NaAlH₄").meltingPointKelvin(451).boilingPointKelvin(504).density(1F).dust().build(),
                MaterialDeclaration.builder("intellite").displayName("Intellite").color(0xD5DDFF).chemicalFormula("Ij").meltingPointKelvin(391).boilingPointKelvin(405).density(1F).metal().build(),
                MaterialDeclaration.builder("MagnesiumDihydroxide").displayName("Magnesium Dihydroxide").color(0xFFFFFF).chemicalFormula("Mg(OH)₂").meltingPointKelvin(623).boilingPointKelvin(3077).density(1F).metal().build(),
                MaterialDeclaration.builder("BariumTitanate").displayName("Barium Titanate").color(0xE6FFB4).chemicalFormula("BaTiO₃").meltingPointKelvin(2373).boilingPointKelvin(3478).density(1F).metal().build(),
                MaterialDeclaration.builder("MolybdenumOxide").displayName("Molybdenum Oxide").color(0xE165EB).chemicalFormula("MoO₂").meltingPointKelvin(1963).boilingPointKelvin(2713).density(1F).metal().liquid().build(),
                MaterialDeclaration.builder("NanoBariumTitanate").displayName("Nano Barium Titanate").color(0x8CAF6E).chemicalFormula("BaTiO₃").meltingPointKelvin(2673).boilingPointKelvin(4078).density(1F).metal().build(),
                MaterialDeclaration.builder("AluminumChloride").displayName("Aluminum Chloride").color(0x8CAF6E).chemicalFormula("AlCl₃").meltingPointKelvin(457).boilingPointKelvin(465).density(1F).dust().build(),
                MaterialDeclaration.builder("BariumAcetate").displayName("Sodium Sulfate").color(0xD3DFD3).chemicalFormula("(CH₃COO)₂Ba").meltingPointKelvin(703).boilingPointKelvin(927).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("SodiumAcetate").displayName("Sodium Acetate").color(0xD3D5D3).chemicalFormula("CH₃COONa").meltingPointKelvin(597).boilingPointKelvin(599).density(1F).dust().build(),
                MaterialDeclaration.builder("PhosphorusPentoxide").displayName("Phosphorus Pentoxide").color(0x6F7B6F).chemicalFormula("P₂O₅").meltingPointKelvin(603).boilingPointKelvin(1627).density(1F).dust().build(),
                MaterialDeclaration.builder("Ketene").displayName("Ketene").color(0xD3DFD3).chemicalFormula("CH₂=C=O").meltingPointKelvin(283).boilingPointKelvin(305).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("TitanicSulfate").displayName("Titanic Sulfate").color(0xE77BC9).chemicalFormula("Ti(SO₄)₂").meltingPointKelvin(2073).boilingPointKelvin(2364).density(1F).dust().build(),
                MaterialDeclaration.builder("TitaniumAcylate").displayName("Titanium Acylate").color(0xE77BC9).chemicalFormula("[TiO₂(OH)(H₂O)₄]").meltingPointKelvin(2073).boilingPointKelvin(2364).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("Trimethylamine").displayName("Trimethylamine").color(0xFFFFFF).chemicalFormula("C₃H₉N").meltingPointKelvin(156).boilingPointKelvin(277).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("Dimethylamine").displayName("Dimethylamine").color(0xFFFFFF).chemicalFormula("C₂H₇N").meltingPointKelvin(180).boilingPointKelvin(280).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("DimethylaminomethylStyrene").displayName("Dimethylaminomethyl Styrene").color(0xFFFFFF).chemicalFormula("C₁₁H₁₅N").meltingPointKelvin(350).boilingPointKelvin(490).density(1F).dust().build(),
                MaterialDeclaration.builder("VinylbenzylChloride").displayName("Vinylbenzyl Chloride").color(0xFFFFFF).chemicalFormula("C₉H₉Cl").meltingPointKelvin(257).boilingPointKelvin(502).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("EthylChloroacetate").displayName("Ethyl Chloroacetate").color(0xFFFFFF).chemicalFormula("C₄H₇ClO₂").meltingPointKelvin(247).boilingPointKelvin(416).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("Dimethylglycine").displayName("Dimethylglycine").color(0xFFFFFF).chemicalFormula("C₄H₉NO₂").meltingPointKelvin(253).boilingPointKelvin(466).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("DinhexylSulfide").displayName("Dinhexyl Sulfide").color(0xFFFFFF).chemicalFormula("C₁₂H₂₆S").meltingPointKelvin(255).boilingPointKelvin(503).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("Hexane").displayName("Hexane").color(0xFFFFFF).chemicalFormula("C₆H₁₄").meltingPointKelvin(178).boilingPointKelvin(342).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("BakedPlatinumGroupSludge").displayName("Baked PlatinumGroup Sludge").color(0xFFFFFF).chemicalFormula("").meltingPointKelvin(1273).boilingPointKelvin(3273).density(1F).dust().build(),
                MaterialDeclaration.builder("OsmiumTetraoxide").displayName("Osmium Tetraoxide").color(0x9B9BE1).chemicalFormula("OsO₄").meltingPointKelvin(313).boilingPointKelvin(405).density(1F).dust().build(),
                MaterialDeclaration.builder("AmmoniumHexachlororhodate").displayName("AmmoniumHexachlororhodate").color(0xFFFFFF).chemicalFormula("(NH₄)₂RhCl₆").meltingPointKelvin(1073).boilingPointKelvin(1473).density(1F).dust().build(),
                MaterialDeclaration.builder("AmmoniumHexachloroiridate").displayName("AmmoniumHexachloroiridate").color(0xFFFFFF).chemicalFormula("(NH₄)₂IrCl₆").meltingPointKelvin(1073).boilingPointKelvin(1473).density(1F).dust().build(),
                MaterialDeclaration.builder("AmmoniumHexachloropalladate").displayName("AmmoniumHexachloropalladate").color(0xFFFFFF).chemicalFormula("(NH₄)₂PdCl₆").meltingPointKelvin(1073).boilingPointKelvin(1473).density(1F).dust().build(),
                MaterialDeclaration.builder("SilverChloride").displayName("Silver Chloride").color(0xFFFFFF).chemicalFormula("AgCl").meltingPointKelvin(728).boilingPointKelvin(1823).density(1F).dust().build(),
                MaterialDeclaration.builder("Methylamine").displayName("Methylamine").color(0xFFFFFF).chemicalFormula("C₂H₇N").meltingPointKelvin(201).boilingPointKelvin(285).density(1F).dust().liquid().build(),
                MaterialDeclaration.builder("Rhodiumoxide").displayName("Rhodiumoxide").color(0xDF9547).chemicalFormula("Rh₂O₃").meltingPointKelvin(1373).boilingPointKelvin(1423).density(1F).dust().build(),
                MaterialDeclaration.builder("Iridiumoxide").displayName("Iridiumoxide").color(0x5B504A).chemicalFormula("IrO₂").meltingPointKelvin(1473).boilingPointKelvin(1973).density(1F).dust().build(),
                MaterialDeclaration.builder("Palladiumoxide").displayName("Palladiumoxide").color(0x3B3734).chemicalFormula("PdO₄").meltingPointKelvin(1143).boilingPointKelvin(1873).density(1F).dust().build(),
                MaterialDeclaration.builder("MirrorGold").displayName("Mirror Gold").color(0xE6DC80).chemicalFormula("Mg").meltingPointKelvin(1337).boilingPointKelvin(3129).density(1F).metal().build(),
                MaterialDeclaration.builder("QuantumGold").displayName("Quantum Gold").color(0xB460FF).chemicalFormula("Qg").meltingPointKelvin(1553).boilingPointKelvin(4369).density(1F).metal().build(),
                MaterialDeclaration.builder("Fluorobenzene").displayName("Fluorobenzene").color(0xDCEBF5).chemicalFormula("C₆H₅F").meltingPointKelvin(232).boilingPointKelvin(358).density(1F).liquid().build(),
                MaterialDeclaration.builder("Fluorotoluene").displayName("4-Fluorotoluene").color(0xE1F0F5).chemicalFormula("C₇H₇F").meltingPointKelvin(178).boilingPointKelvin(389).density(1F).liquid().build(),
                MaterialDeclaration.builder("FluorobenzoicAcid").displayName("4-Fluorobenzoic Acid").color(0xF5F5F5).chemicalFormula("C₇H₅FO₂").meltingPointKelvin(456).boilingPointKelvin(526).density(1F).dust().build(),
                MaterialDeclaration.builder("FluorobenzoylChloride").displayName("4-Fluorobenzoyl Chloride").color(0xEBF5D2).chemicalFormula("C₇H₄ClFO").meltingPointKelvin(263).boilingPointKelvin(467).density(1F).liquid().build(),
                MaterialDeclaration.builder("Difluorobenzophenone").displayName("4,4'-Difluorobenzophenone").color(0xF0EBDC).chemicalFormula("C₁₃H₈F₂O").meltingPointKelvin(380).boilingPointKelvin(648).density(1F).dust().build(),
                MaterialDeclaration.builder("Hydroquinone").displayName("Hydroquinone").color(0xF0F0F0).chemicalFormula("C₆H₆O₂").meltingPointKelvin(445).boilingPointKelvin(560).density(1F).dust().build(),
                MaterialDeclaration.builder("PEEK").displayName("Polyether Ether Ketone").color(0xD2BEA0).chemicalFormula("(C₁₉H₁₂O₃)n").meltingPointKelvin(616).boilingPointKelvin(873).density(1F).dust().polymer().build(),
                // flList.YttriumZirconiumOxide. The source register call sets no color or formula.
                // Its fluid temperature is 2700 and is not a melting point, so those stay at the builder defaults.
                MaterialDeclaration.builder("YttriumZirconiumOxide").displayName("Yttrium-Zirconium Oxide").liquid().build(),
                // flList aqueous oils. Temperature 277 K is the source fluid temperature.
                MaterialDeclaration.builder("AqueousOilExtraHeavy").displayName("Aqueous Very Heavy Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("AqueousOilHeavy").displayName("Aqueous Heavy Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("AqueousOilMedium").displayName("Aqueous Medium Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("AqueousOilLight").displayName("Aqueous Light Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                // flList.RecycledFuel0-4. The source register calls set no color or temperature.
                MaterialDeclaration.builder("RecycledFuel0").displayName("#0 Recycled Fuel").liquid().build(),
                MaterialDeclaration.builder("RecycledFuel1").displayName("#1 Recycled Fuel").liquid().build(),
                MaterialDeclaration.builder("RecycledFuel2").displayName("#2 Recycled Fuel").liquid().build(),
                MaterialDeclaration.builder("RecycledFuel3").displayName("#3 Recycled Fuel").liquid().build(),
                MaterialDeclaration.builder("RecycledFuel4").displayName("#4 Recycled Fuel").liquid().build(),
                // flList.Bittern, not a materialPreInit row. The tiny tower's ocean recipe outputs it.
                // The source register call sets no color or temperature, so those stay at the builder defaults.
                MaterialDeclaration.builder("Bittern").displayName("Bittern").liquid().build(),
                // Remaining standalone flList fluids from fluidPreInit.java.
                MaterialDeclaration.builder("SodiumHeterotungstate").displayName("Sodium Heterotungstate").liquid().build(),
                MaterialDeclaration.builder("AmmoniumTungstate").displayName("Ammonium Tungstate").liquid().build(),
                MaterialDeclaration.builder("Methoxymethane").displayName("Methoxymethane").liquid().build(),
                MaterialDeclaration.builder("Phenol").displayName("Phenol").liquid().build(),
                MaterialDeclaration.builder("AllylAlcohol").displayName("Allyl Alcohol").liquid().build(),
                MaterialDeclaration.builder("AllylAcetate").displayName("Allyl Acetate").liquid().build(),
                MaterialDeclaration.builder("MethacrylicAcid").displayName("Methacrylic Acid").liquid().build(),
                MaterialDeclaration.builder("GlacialAceticAcid").displayName("Glacial Acetic Acid").liquid().build(),
                MaterialDeclaration.builder("AqueousOilNormal").displayName("Aqueous Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("DesaltOilExtraHeavy").displayName("Desalinized Very Heavy Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("DesaltOilHeavy").displayName("Desalinized Heavy Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("DesaltOilMedium").displayName("Desalinized Medium Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("DesaltOilNormal").displayName("Desalinized Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("DesaltOilLight").displayName("Desalinized Light Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("OilDesulfurizationer").displayName("Oil Desulphurizationer").liquid().build(),
                MaterialDeclaration.builder("SulfuredOilDesulfurizationer").displayName("Sulfured Oil Desulphurizationer").liquid().build(),
                MaterialDeclaration.builder("OilGas").displayName("Oil Gas").gas().build(),
                MaterialDeclaration.builder("CrackedOilGas").displayName("Cracked Oil Gas").gas().build(),
                MaterialDeclaration.builder("InitalBottomOil").displayName("Initial Bottom Oil").liquid().build(),
                MaterialDeclaration.builder("Naphtha").displayName("Naphtha").liquid().build(),
                MaterialDeclaration.builder("WoodTar").displayName("Wood Tar").liquid().build(),
                MaterialDeclaration.builder("CoalTar").displayName("Coal Tar").liquid().build(),
                MaterialDeclaration.builder("CleanedOilExtraHeavy").displayName("Cleaned Very Heavy Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("CleanedOilHeavy").displayName("Cleaned Heavy Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("CleanedOilMedium").displayName("Cleaned Medium Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("CleanedOilNormal").displayName("Cleaned Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("CleanedOilLight").displayName("Cleaned Light Oil").meltingPointKelvin(277).boilingPointKelvin(350).density(1F).liquid().build(),
                MaterialDeclaration.builder("BlendedFuel1").displayName("Blended Fuel 1").liquid().build(),
                MaterialDeclaration.builder("BlendedFuel2").displayName("Blended Fuel 2").liquid().build(),
                MaterialDeclaration.builder("BlendedFuel3").displayName("Blended Fuel 3").liquid().build(),
                MaterialDeclaration.builder("BlendedFuel4").displayName("Blended Fuel 4").liquid().build(),
                MaterialDeclaration.builder("BlendedFuel5").displayName("Blended Fuel 5").liquid().build(),
                MaterialDeclaration.builder("BioFuel1").displayName("Bio Fuel 1").liquid().build(),
                MaterialDeclaration.builder("BioFuel2").displayName("Bio Fuel 2").liquid().build(),
                MaterialDeclaration.builder("EtchingSolution").displayName("Etching Solution").liquid().build(),
                MaterialDeclaration.builder("NegativeColloid").displayName("Negative Colloid").liquid().build(),
                MaterialDeclaration.builder("PositiveColloid").displayName("Positive Colloid").liquid().build(),
                MaterialDeclaration.builder("SolutionBPASodium").displayName("BPA & Sodium Solution").liquid().build(),
                MaterialDeclaration.builder("MoltenTeflon").displayName("Molten Teflon").liquid().build(),
                MaterialDeclaration.builder("MoltenBakelite").displayName("Molten Bakelite").liquid().build(),
                MaterialDeclaration.builder("MoltenPolycarbonate").displayName("Molten Polycarbonate").liquid().build(),
                MaterialDeclaration.builder("UraniumExtractant").displayName("Uranium Extractant").liquid().build(),
                MaterialDeclaration.builder("UsedUraniumExtractant").displayName("Used Uranium Extractant").liquid().build(),
                MaterialDeclaration.builder("ExtractedUranium").displayName("Extracted Uranium").liquid().build(),
                MaterialDeclaration.builder("ExtractedUranium2").displayName("Extracted Uranium 2").liquid().build(),
                MaterialDeclaration.builder("BariumSulfate").displayName("Barium Sulfate").liquid().build(),
                MaterialDeclaration.builder("Diketene").displayName("Diketene").liquid().build(),
                MaterialDeclaration.builder("ImpureTitaniumAcylate").displayName("Impure Titanium Acylate").liquid().build(),
                MaterialDeclaration.builder("CrudeTitaniumAcylate").displayName("Crude Titanium Acylate").liquid().build(),
                MaterialDeclaration.builder("PurifyTitaniumAcylate").displayName("Purified Titanium Acylate").liquid().build(),
                MaterialDeclaration.builder("RoughPlatinumGroupSolution").displayName("Rough Processed Platinum Group Solution").liquid().build(),
                MaterialDeclaration.builder("SulphurPlatinumGroupSolution").displayName("Sulphur Processed Platinum Group Solution").liquid().build(),
                MaterialDeclaration.builder("ExtractedPlatinumGroupSolutionUp").displayName("Extracted Platinum Group Solution Up").liquid().build(),
                MaterialDeclaration.builder("AcidPlatinumGroupSolutionUp").displayName("Acid Platinum Group Solution Up").liquid().build(),
                MaterialDeclaration.builder("DinhexylSulfidePlatinumGroupSolution").displayName("Dinhexyl Sulfide Platinum Group Solution").liquid().build(),
                MaterialDeclaration.builder("ExtractedPlatinumGroupSolutionDown").displayName("Extracted Platinum Group Solution Down").liquid().build(),
                MaterialDeclaration.builder("DistilledPlatinumGroupSolutionDown").displayName("Distilled Platinum Group Solution Down").liquid().build(),
                MaterialDeclaration.builder("NitricPlatinumGroupSolutionUp").displayName("Nitric Platinum Group Solution Up").liquid().build(),
                MaterialDeclaration.builder("DeprecatedNitricPlatinumGroupSolutionUp").displayName("Deprecated Nitric Platinum Group Solution Up").liquid().build(),
                MaterialDeclaration.builder("NitricPlatinumGroupSolutionDown").displayName("Nitric Platinum Group Solution Down").liquid().build(),
                MaterialDeclaration.builder("ElectrolyzedNitricPlatinumGroupSolution").displayName("Electrolyzed Nitric Platinum Group Solution").liquid().build(),
                MaterialDeclaration.builder("IridiumPlatinumGroupSolution").displayName("Iridium Platinum Group Solution").liquid().build(),
                MaterialDeclaration.builder("HighCarbonExhaust").displayName("High-Carbon Mixed Exhaust").gas().build(),
                MaterialDeclaration.builder("HighWaterExhaust").displayName("High-Water Mixed Exhaust").gas().build(),
                MaterialDeclaration.builder("NitrogenHighCarbonExhaust").displayName("Nitrogen-Rich High-Carbon Mixed Exhaust").gas().build(),
                MaterialDeclaration.builder("NitrogenHighWaterExhaust").displayName("Nitrogen-Rich High-Water Mixed Exhaust").gas().build(),
                MaterialDeclaration.builder("CrackedNaphthaLow").displayName("Cracked Naphtha (Low Temperature)").liquid().build(),
                MaterialDeclaration.builder("CrackedNaphthaMedium").displayName("Cracked Naphtha (Medium Temperature)").liquid().build(),
                MaterialDeclaration.builder("CrackedNaphthaHigh").displayName("Cracked Naphtha (High Temperature)").liquid().build(),
                MaterialDeclaration.builder("CrackedPetrolLow").displayName("Cracked Petrol (Low Temperature)").liquid().build(),
                MaterialDeclaration.builder("CrackedPetrolMedium").displayName("Cracked Petrol (Medium Temperature)").liquid().build(),
                MaterialDeclaration.builder("CrackedPetrolHigh").displayName("Cracked Petrol (High Temperature)").liquid().build(),
                MaterialDeclaration.builder("BrineMgFree").displayName("Magnesium Free Brine").liquid().build(),
                MaterialDeclaration.builder("BrineBrRich").displayName("Bromine Rich Brine").liquid().build(),
                MaterialDeclaration.builder("BrineWaste").displayName("Waste Brine").liquid().build(),
                MaterialDeclaration.builder("Butynediol").displayName("Butynediol").liquid().build(),
                MaterialDeclaration.builder("Butanediol").displayName("Butanediol").liquid().build(),
                MaterialDeclaration.builder("Butyrolactone").displayName("Butyrolactone").liquid().build(),
                MaterialDeclaration.builder("Methylpyrrolidone").displayName("Methylpyrrolidone").liquid().build(),
                MaterialDeclaration.builder("Graphite_Methylpyrrolidone").displayName("Graphite-Methylpyrrolidone").liquid().build()
        );
    }

    @Override public List<MaterialFormRequest> forms() {
        return List.of(
                new MaterialFormRequest("SiliconeRubber", "plate"),
                new MaterialFormRequest("SiliconeRubber", "foil"),
                new MaterialFormRequest("SBR", "plate"),
                new MaterialFormRequest("SBR", "foil"),
                new MaterialFormRequest("PEEK", "plate"),
                new MaterialFormRequest("PEEK", "foil"));
    }

    /**
     * prefixList at 6404ab69. Flywheel uses the tool-and-smithable filter. Blades and
     * the six turbine prefixes use the source's large-turbine condition: tool stats,
     * molten, and metal (all GT alloys also carry METAL), minus annealed copper,
     * wrought iron and alumite. Purity dusts list the refining families; the source
     * condition is negative-only, which an empty material list cannot express.
     */
    @Override public List<PrefixDeclaration> prefixes() {
        Set<PrefixCondition> tool = Set.of(PrefixCondition.HAS_TOOL_STATS, PrefixCondition.SMITHABLE);
        Set<PrefixCondition> turbine = Set.of(PrefixCondition.HAS_TOOL_STATS, PrefixCondition.MOLTEN,
                PrefixCondition.METAL);
        List<String> turbineExcluded = List.of("AnnealedCopper", "WroughtIron", "Alumite");
        // These six materials already name a flywheel machine block, so the prefix
        // item would register a second item under the same id.
        List<String> flywheelExcluded = List.of(
                "Wood", "Bronze", "Steel", "Titanium", "Tungstensteel", "Adamantium");
        // GT6CE does not flag these two as tool-statted metal, but the source turbine rows use them.
        List<String> turbineMaterials = List.of("Graphene", "Adamantium");
        List<String> purity = List.of(
                "Lithium", "Iron", "Nickel", "Cobalt", "Copper", "Zinc", "Silver", "Tin", "Lead", "Bismuth",
                "Aluminium", "Titanium", "Zirconium", "Niobium", "Tantalum", "Chromium",
                "Gold", "Platinum", "Palladium", "Rhodium", "Iridium", "OsmiumElemental", "intellite",
                "Tungsten", "Molybdenum",
                "Carbon", "Silicon", "Germanium", "Gallium", "Indium",
                "Deuterium", "Tritium", "Uranium", "Thorium", "Naquadria");
        return List.of(
                prefix("flywheel", "", " Flywheel", 8, "item/material_icons/flywheel", List.of(), tool, flywheelExcluded),
                prefix("bladeLargeTurbine", "Large ", " Turbine Blade", 2, "item/material_icons/blade_large_turbine", turbineMaterials, turbine, turbineExcluded),
                prefix("turbineLargeGas", "Large ", " Gas Turbine", 72, "item/material_icons/turbine_large", turbineMaterials, turbine, turbineExcluded),
                prefix("turbineLargeGasChecked", "Checked Large ", " Gas Turbine", 72, "item/material_icons/turbine_large", turbineMaterials, turbine, turbineExcluded),
                prefix("turbineLargeGasDamaged", "Used Large ", " Gas Turbine", 72, "item/material_icons/turbine_large", turbineMaterials, turbine, turbineExcluded),
                prefix("turbineLargeSteam", "Large ", " Steam Turbine", 48, "item/material_icons/turbine_large", turbineMaterials, turbine, turbineExcluded),
                prefix("turbineLargeSteamChecked", "Checked Large ", " Steam Turbine", 48, "item/material_icons/turbine_large", turbineMaterials, turbine, turbineExcluded),
                prefix("turbineLargeSteamDamaged", "Used Large ", " Steam Turbine", 48, "item/material_icons/turbine_large", turbineMaterials, turbine, turbineExcluded),
                prefix("dustCommercialPure", "Commercial Pure ", " Dust", 1, "item/material_icons/dust_commercial_pure", purity, Set.of()),
                prefix("dustAnalyticalPure", "Analytical Pure ", " Dust", 1, "item/material_icons/dust_analytical_pure", purity, Set.of()),
                prefix("dustAbsolutelyPure", "Absolutely Pure ", " Dust", 1, "item/material_icons/dust_absolutely_pure", purity, Set.of()));
    }

    private static PrefixDeclaration prefix(String name, String displayPrefix, String displaySuffix, long units,
            String texture, List<String> materials, Set<PrefixCondition> conditions) {
        return new PrefixDeclaration(name, displayPrefix, displaySuffix, units, texture, materials, conditions);
    }

    private static PrefixDeclaration prefix(String name, String displayPrefix, String displaySuffix, long units,
            String texture, List<String> materials, Set<PrefixCondition> conditions, List<String> excluded) {
        return new PrefixDeclaration(name, displayPrefix, displaySuffix, units, texture, materials, conditions, excluded);
    }
}
