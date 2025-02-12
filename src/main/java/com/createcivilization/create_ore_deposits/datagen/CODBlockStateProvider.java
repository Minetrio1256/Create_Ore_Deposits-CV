package com.createcivilization.create_ore_deposits.datagen;


import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.block.CODBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class CODBlockStateProvider extends BlockStateProvider {
    public CODBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CreateOreDeposits.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(CODBlocks.DRILL_BLOCK);
        blockWithItem(CODBlocks.IRON_ORE_DEPOSIT_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
