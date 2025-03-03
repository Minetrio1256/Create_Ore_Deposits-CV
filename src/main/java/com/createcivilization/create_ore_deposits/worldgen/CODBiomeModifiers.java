package com.createcivilization.create_ore_deposits.worldgen;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class CODBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_IRON_ORE_DEPOSITS = registerKey("add_iron_ore_deposits");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_IRON_ORE_DEPOSITS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(CODPlacedFeatures.IRON_DEPOSIT_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
    }



    private static ResourceKey<BiomeModifier> registerKey(String name){
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CreateOreDeposits.MOD_ID, name));
    }
}
