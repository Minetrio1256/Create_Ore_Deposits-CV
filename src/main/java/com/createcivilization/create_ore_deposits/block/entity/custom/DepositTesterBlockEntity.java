package com.createcivilization.create_ore_deposits.block.entity.custom;

import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.block.entity.custom.base.*;

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

    private int startTick = 1;

    public void tick(Level level, BlockPos pos, BlockState state) {
        this.setEfficiency(10); // Wait what but don't we need these values to be variable??
        this.setResourcePullSpeed(20);
        if (this.startTick % this.getResourcePullSpeed() != 0) {
            this.startTick++;
            return;
        }
        this.startTick++;
        if (level instanceof ServerLevel serverLevel) {
            BlockPos below = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
            if (!this.hasTarget() && this.isBlockDeposit(serverLevel, below)) {
                BlockPos furthestBlock = this.findFurthestTarget(level, below);
                this.setHasTarget(true);
                this.setTargetPos(furthestBlock);
            }
            if (this.hasTarget()) {
                if(!this.isBlockDeposit(serverLevel, below)) {
                    this.setHasTarget(false);
                    this.setBreakingProgressMilestone(-1);
                    return;
                }

                var blockEntity = serverLevel.getBlockEntity(this.getTargetPos());
                if (blockEntity instanceof BaseOreDepositBlockEntity BE) {
                    if (this.getBreakingProgressMilestone() == -1) this.setBreakingProgressMilestone((double) BE.getResourceLevel() / 9);
                    if (BE.getResourceLevel() == 0) {
                        serverLevel.destroyBlock(this.getTargetPos(), false);
                        serverLevel.destroyBlockProgress(1, this.getTargetPos(), 0);

                        this.setBreakingProgressMilestone(-1);
                        this.setHasTarget(false);
                    } else if (BE.getResourceLevel() > 0) {
                        serverLevel.destroyBlockProgress(
                                1,
                                this.getTargetPos(),
                                this.getBreakingProgress(
                                        this.getBreakingProgressMilestone(),
                                        BE.getResourceLevel()
                                )
                        );
                        ItemEntity oreEntity = createItem(
                                serverLevel,
                                this.getBlockPos().above(),
                                BE.getExtractionStack(this.getEfficiency())
                        );
                        serverLevel.addFreshEntity(oreEntity);
                    }
                }
            }
        }
    }

    public static ItemEntity createItem(ServerLevel serverLevel, BlockPos pos, ItemStack stack) {
        if (serverLevel == null) return null;

        Random rand = new Random();
        ItemEntity itemEntity = new ItemEntity(serverLevel, pos.getX(), pos.getY(), pos.getZ(), stack);
        // Why the hell was there pointless maths here??
        itemEntity.setDeltaMovement(rand.nextDouble(5), 2, rand.nextDouble(5));
        return itemEntity;
    }
}