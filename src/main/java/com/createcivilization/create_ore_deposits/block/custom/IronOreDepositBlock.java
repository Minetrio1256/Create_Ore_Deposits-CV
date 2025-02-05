package com.createcivilization.create_ore_deposits.block.custom;

import com.createcivilization.create_ore_deposits.block.entity.custom.IronOreDepositBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class IronOreDepositBlock extends BaseEntityBlock {

    public IronOreDepositBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new IronOreDepositBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
