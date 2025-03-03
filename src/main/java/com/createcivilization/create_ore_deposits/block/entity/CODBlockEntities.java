package com.createcivilization.create_ore_deposits.block.entity;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.block.CODBlocks;
import com.createcivilization.create_ore_deposits.block.entity.custom.*;

import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class CODBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CreateOreDeposits.MOD_ID);

    public static final RegistryObject<BlockEntityType<DrillBlockEntity>> DEPOSIT_TESTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("deposit_tester_block_entity", () ->
                    BlockEntityType.Builder.of(
                            DrillBlockEntity::new,
                            CODBlocks.DRILL_BLOCK.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<TestBlockEntity>> TEST_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("test_block_entity", () ->
                    BlockEntityType.Builder.of(
                            TestBlockEntity::new,
                            CODBlocks.TEST_BLOCK.get()
                    ).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}