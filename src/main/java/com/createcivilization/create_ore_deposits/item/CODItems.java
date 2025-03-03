package com.createcivilization.create_ore_deposits.item;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


public class CODItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateOreDeposits.MOD_ID);

    public static final DeferredItem<Item> TEST_ITEM = ITEMS.registerItem(
            "test_item",
            Item::new, // The factory that the properties will be passed into.
            new Item.Properties() // The properties to use.
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}