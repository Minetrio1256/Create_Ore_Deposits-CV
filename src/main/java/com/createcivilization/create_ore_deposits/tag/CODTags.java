package com.createcivilization.create_ore_deposits.tag;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CODTags {

    public static class Blocks {

        public static final TagKey<Block> ORE_DEPOSITS = registerTag("ore_deposits");

        public static final TagKey<Block> EVERYTHING = registerTag("everything");

        public static TagKey<Block> registerTag(String name) {
            return TagKey.create(net.minecraft.core.registries.Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(CreateOreDeposits.MOD_ID, name));
        }
    }
}
