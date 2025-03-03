package com.createcivilization.create_ore_deposits;

import com.createcivilization.create_ore_deposits.block.CODBlocks;
import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.item.CODItems;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CreateOreDeposits.MOD_ID)
public class CreateOreDeposits {

    public static final String MOD_ID = "create_ore_deposits";
    public static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal") // Forge 47.3.10 makes breaking changes but older versions are shipped to users by default
    public CreateOreDeposits() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CODItems.register(modEventBus);
        CODBlocks.register(modEventBus);
        CODBlockEntities.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}