package com.createcivilization.create_ore_deposits.datagen;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.block.CODBlocks;
import com.createcivilization.create_ore_deposits.tag.CODTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CODBlockTagGenerator extends BlockTagsProvider {

    public CODBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CreateOreDeposits.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        this.tag(CODTags.Blocks.ORE_DEPOSITS)
                .add(CODBlocks.IRON_ORE_DEPOSIT_BLOCK.get());

        //This is so dumb. (also needs to be removed)
        for(Block block : ForgeRegistries.BLOCKS) {
            this.tag(CODTags.Blocks.EVERYTHING).add(block);
        }

    }

    @Override
    public String getName() {
        return "Block Tags";
    }
}
