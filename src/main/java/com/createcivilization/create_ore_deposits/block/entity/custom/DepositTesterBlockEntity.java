package com.createcivilization.create_ore_deposits.block.entity.custom;

import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.block.entity.custom.base.BaseDrillBlockEntity;
import com.createcivilization.create_ore_deposits.block.entity.custom.base.BaseOreDepositBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class DepositTesterBlockEntity extends BaseDrillBlockEntity {

    public DepositTesterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CODBlockEntities.DEPOSIT_TESTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public void tick(Level Level, BlockPos Pos, BlockState State) {
        if(level instanceof ServerLevel serverLevel) {
            BlockPos below = new BlockPos(Pos.getX(), Pos.getY() - 1, Pos.getZ());
            if(!this.target && isBlockDeposit(serverLevel, below)) {
                BlockPos furthestBlock = findFurthestTarget(Level, below);
                this.target = true;
                this.targetPos = furthestBlock;
            }
            if(target) {
                if(!(isBlockDeposit(serverLevel, below))) {
                    this.target = false;
                    this.breakingProgessMilestone = -1;
                    return;
                }

                var blockEntity = serverLevel.getBlockEntity(this.targetPos);
                if(blockEntity instanceof BaseOreDepositBlockEntity BE) {
                    if (this.breakingProgessMilestone == -1){
                        this.breakingProgessMilestone = (double) BE.getResourceLevel() / 9;
                    }
                    if(BE.getResourceLevel() == 0){
                        serverLevel.destroyBlock(this.targetPos, false);
                        serverLevel.destroyBlockProgress(1, this.targetPos, 0);

                        this.breakingProgessMilestone = -1;
                        target = false;
                    } else if(BE.getResourceLevel() > 0){
                        serverLevel.destroyBlockProgress(1, this.targetPos, getBreakingProgress(this.breakingProgessMilestone, BE.getResourceLevel()));
                        ItemEntity oreEntity = createItem(serverLevel, this.getBlockPos().above(), BE.getExtractionStack(1));
                        serverLevel.addFreshEntity(oreEntity);
                    }
                }
            }
        }
    }

    public static ItemEntity createItem(ServerLevel serverLevel, BlockPos pos, ItemStack stack) {
        if (serverLevel == null) return null;

        Random rand = new Random();

        ItemEntity itemEntity = new ItemEntity(
                serverLevel, pos.getX(), pos.getY(), pos.getZ(), stack
        );
        itemEntity.setDeltaMovement(rand.nextDouble(((3 - 0) + 1) + 1),2,rand.nextDouble(((3 - 0) + 1) + 1));
        return itemEntity;
    }

}
