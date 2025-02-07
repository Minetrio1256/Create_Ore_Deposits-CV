package com.createcivilization.create_ore_deposits.block.entity.custom;

import com.createcivilization.create_ore_deposits.block.CODBlocks;
import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.block.entity.custom.base.BaseDrillBlockEntity;
import com.createcivilization.create_ore_deposits.block.entity.custom.base.BaseOreDepositBlockEntity;
import com.createcivilization.create_ore_deposits.util.CODTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class DepositTesterBlockEntity extends BaseDrillBlockEntity {

    private boolean target = false;
    private BlockPos targetPos = new BlockPos(0, 0, 0);

    private double breakingProgessMilestone = -1;

    public DepositTesterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CODBlockEntities.DEPOSIT_TESTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public void tick(Level Level, BlockPos Pos, BlockState State) {
        if(level instanceof ServerLevel serverLevel) {
            BlockPos below = new BlockPos(Pos.getX(), Pos.getY() - 1, Pos.getZ());
            if(!this.target && isBlockDepositMatch(serverLevel, below)) {
                BlockPos furthestBlock = findFurthestTarget(Level, below);
                this.target = true;
                this.targetPos = furthestBlock;
            }
            if(target) {
                if(!(isBlockDepositMatch(serverLevel, below))) {
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

    public static int getBreakingProgress(double breakingProgessMilestone, int resourceLevel) {
        double progressRatio = (double) resourceLevel / breakingProgessMilestone;
        int progress = 9 - (int) Math.ceil(progressRatio);
        return Math.min(9, Math.max(1, progress));
    }

    public void setTargetPos(int[] targetPosArray) {
        this.targetPos = new BlockPos(targetPosArray[0], targetPosArray[1], targetPosArray[2]);
    }

    public BlockPos getTargetPos() {
        return targetPos;
    }

    public void setHasTarget(boolean hasTarget) {
        this.target = hasTarget;
    }

    public boolean hasTarget() {
        return target;
    }

    @Override
    public void load(CompoundTag pTag) {
        this.setHasTarget(pTag.getBoolean("HasTarget"));
        this.setTargetPos(pTag.getIntArray("TargetPos"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putBoolean("HasTarget", this.hasTarget());
        pTag.putIntArray("TargetPos", new int[]{this.getTargetPos().getX(), this.getTargetPos().getY(), this.getTargetPos().getZ()});
    }
}
