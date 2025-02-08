package com.createcivilization.create_ore_deposits.tag;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.level.block.Block;

public class CODTags {

    public static class Blocks {

        public static final TagKey<Block> ORE_DEPOSITS = registerTag("ore_deposits");

        public static TagKey<Block> registerTag(String name) {
            return BlockTags.create(new ResourceLocation(CreateOreDeposits.MOD_ID, name));
        }
    }
}