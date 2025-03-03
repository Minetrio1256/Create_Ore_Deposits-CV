package com.createcivilization.create_ore_deposits;

import com.createcivilization.create_ore_deposits.block.CODBlocks;
import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.item.CODItems;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(CreateOreDeposits.MOD_ID)
public class CreateOreDeposits {

    public static final String MOD_ID = "create_ore_deposits";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateOreDeposits(IEventBus modEventBus, ModContainer modContainer) {

        CODItems.register(modEventBus);
        CODBlocks.register(modEventBus);
        CODBlockEntities.register(modEventBus);
        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
    }

}