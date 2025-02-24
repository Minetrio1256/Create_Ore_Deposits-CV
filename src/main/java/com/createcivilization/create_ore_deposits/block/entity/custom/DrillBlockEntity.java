package com.createcivilization.create_ore_deposits.block.entity.custom;

import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.block.entity.custom.base.*;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DrillBlockEntity extends BaseDrillBlockEntity {

    public DrillBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CODBlockEntities.DEPOSIT_TESTER_BLOCK_ENTITY.get(), pPos, pBlockState);
        setEfficiency(10);
        setResourcePullSpeed(20);
    }

    //Can add some on rotation speed change events here and etc
}