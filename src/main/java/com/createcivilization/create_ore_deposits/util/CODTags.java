package com.createcivilization.create_ore_deposits.util;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CODTags {
    public static class Blocks {

        public static final TagKey<Block> ORE_DEPOSITS = tag("ore_deposits");

        private static TagKey<Block> tag(String name){
            return BlockTags.create(new ResourceLocation(CreateOreDeposits.MODID, name));
        }
    }

}
