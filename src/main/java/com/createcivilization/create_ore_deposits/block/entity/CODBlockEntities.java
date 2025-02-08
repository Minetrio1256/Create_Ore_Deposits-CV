package com.createcivilization.create_ore_deposits.block.entity;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.block.CODBlocks;
import com.createcivilization.create_ore_deposits.block.entity.custom.*;

import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class CODBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CreateOreDeposits.MOD_ID);

    public static final RegistryObject<BlockEntityType<IronOreDepositBlockEntity>> IRON_ORE_DEPOSIT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("iron_ore_deposit_block_entity", () ->
                    BlockEntityType.Builder.of(
                            IronOreDepositBlockEntity::new,
                            CODBlocks.IRON_ORE_DEPOSIT_BLOCK.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<DepositTesterBlockEntity>> DEPOSIT_TESTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("deposit_tester_block_entity", () ->
                    BlockEntityType.Builder.of(
                            DepositTesterBlockEntity::new,
                            CODBlocks.DEPOSIT_TESTER_BLOCK.get()
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