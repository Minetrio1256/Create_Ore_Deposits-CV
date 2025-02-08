package com.createcivilization.create_ore_deposits.block.entity.custom;

import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.block.entity.custom.base.BaseOreDepositBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;

public class IronOreDepositBlockEntity extends BaseOreDepositBlockEntity {

    public IronOreDepositBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CODBlockEntities.IRON_ORE_DEPOSIT_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public Item getExtractionItem() {
        return Items.RAW_IRON;
    }
}