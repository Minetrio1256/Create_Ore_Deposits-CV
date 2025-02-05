package com.createcivilization.create_ore_deposits.block;

import com.createcivilization.create_ore_deposits.Create_ore_deposits;
import com.createcivilization.create_ore_deposits.block.custom.DepositTesterBlock;
import com.createcivilization.create_ore_deposits.block.custom.IronOreDepositBlock;
import com.createcivilization.create_ore_deposits.item.CODItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CODBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Create_ore_deposits.MODID);



    public static RegistryObject<Block> IRON_ORE_DEPOSIT_BLOCK = registerBlock(
            "iron_ore_deposit_block", () -> new IronOreDepositBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE))
    );

    public static RegistryObject<Block> DEPOSIT_TESTER_BLOCK = registerBlock(
            "deposit_tester_block", () -> new DepositTesterBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK))
    );


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        CODItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }



    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }


}
