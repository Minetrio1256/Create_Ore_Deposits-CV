package com.createcivilization.create_ore_deposits.block.custom;

import com.createcivilization.create_ore_deposits.block.entity.custom.IronOreDepositBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// @ParametersAreNonNullByDefault is so fucking annoying
@SuppressWarnings("NullableProblems")
public class IronOreDepositBlock extends BaseEntityBlock {

    public IronOreDepositBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new IronOreDepositBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}