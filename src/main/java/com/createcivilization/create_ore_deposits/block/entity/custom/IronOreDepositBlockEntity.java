package com.createcivilization.create_ore_deposits.block.entity.custom;

import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.block.entity.custom.base.BaseOreDepositBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class IronOreDepositBlockEntity extends BaseOreDepositBlockEntity {

    Random random = new Random();


    public IronOreDepositBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CODBlockEntities.IRON_ORE_DEPOSIT_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.setResourceLevel(random.nextInt(150,301));
    }

    @Override
    public Item getExtractionItem() {
        return Items.RAW_IRON;
    }

}