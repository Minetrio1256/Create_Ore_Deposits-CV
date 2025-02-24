package com.createcivilization.create_ore_deposits.block;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.block.custom.*;
import com.createcivilization.create_ore_deposits.block.custom.gen.BaseGeneratedDepositOre;
import com.createcivilization.create_ore_deposits.item.CODItems;

import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class CODBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CreateOreDeposits.MOD_ID);

    public static RegistryObject<Block> DRILL_BLOCK = registerBlock(
            "deposit_tester_block", () -> new DrillBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK))
    ); //TODO: Figure out a name for the drill

    public static RegistryObject<Block> TEST_BLOCK = registerBlock(
            "test_block", () -> new TestBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK))
    );

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        CODItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {

        // TODO:: DO NOT TOUCH THIS, for now its debug but I shall, and I repeat **I**, Orion, shall make a better system for this, DO NOT TOUCH!!!! <3
        for (Block ore : ForgeRegistries.BLOCKS.getValues().stream().filter(block -> block.getName().toString().toLowerCase().contains("ore")).toList()) {
            registerBlock(
                    ForgeRegistries.BLOCKS.getKey(ore).getPath(),
                    () -> new BaseGeneratedDepositOre(ore)
            );
        }
        BLOCKS.register(eventBus);
    }
}