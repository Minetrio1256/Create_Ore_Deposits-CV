package com.createcivilization.create_ore_deposits.block.entity.custom;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.block.custom.gen.BaseGeneratedDepositOre;
import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.block.entity.custom.base.*;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class DrillBlockEntity extends BaseDrillBlockEntity {

    public DrillBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CODBlockEntities.DEPOSIT_TESTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    private int startTick = 1;

    public void onTick() {
        //TODO -> Embed this code in BaseDrillBlockEntity so that we don't have to copy it for different tiers
        //TODO -> Make varying speeds affect the drill (ASK ARTY ON THE MATHS FOR THAT)
        ServerLevel serverLevel = (ServerLevel) this.level;
        assert serverLevel != null;
        BlockPos pos = this.getBlockPos();
        this.setEfficiency(10); // Wait what but don't we need these values to be variable??
        this.setResourcePullSpeed(20);
        if (this.startTick % this.getResourcePullSpeed() != 0) {
            this.startTick++;
            return;
        }
        this.startTick++;

        ItemStack slot = this.inventory.getStackInSlot(0);

        int maxStackSize = slot.getMaxStackSize();
        int count = slot.getCount();
        if (count >= maxStackSize) return;

        BlockPos below = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());

        if (!this.hasTarget() && this.isBlockDeposit(serverLevel, below)) {
            BlockPos furthestBlock = this.findFurthestTarget(serverLevel, below);
            this.setHasTarget(true);
            this.setTargetPos(furthestBlock);
            this.setChanged(); //Mark the drill for saving
        }

        if (this.hasTarget() && !this.isBlockDeposit(serverLevel, this.getTargetPos())) {
            this.setHasTarget(false);
            this.setBreakingProgressMilestone(-1);
            this.setChanged(); //Mark the drill for saving
        } else if (this.hasTarget() && this.isBlockDeposit(serverLevel, this.getTargetPos())) {

            BlockState targetBlockState = level.getBlockState(this.getTargetPos());
            BaseGeneratedDepositOre ore = (BaseGeneratedDepositOre) targetBlockState.getBlock();

            int efficiency = this.getEfficiency();
            int realEfficiency = Math.min(count + efficiency, maxStackSize);
            ItemStack extractedItem = ore.extractItemStack(serverLevel, realEfficiency);
            int resourceValue = targetBlockState.getValue(BaseGeneratedDepositOre.RESOURCE_VALUE);
            if (!slot.isEmpty() && !slot.getItem().equals(extractedItem.getItem())) return;
            if (this.getBreakingProgressMilestone() == -1) this.setBreakingProgressMilestone((double) resourceValue / 9);
            if (resourceValue > 0) {
                serverLevel.destroyBlockProgress(
                        1,
                        this.getTargetPos(),
                        this.getBreakingProgress(
                                this.getBreakingProgressMilestone(),
                                resourceValue
                        )
                );

                inventory.setStackInSlot(0, extractedItem);
                BlockState newState = targetBlockState.setValue(BaseGeneratedDepositOre.RESOURCE_VALUE, Math.max(0, resourceValue - efficiency));
                serverLevel.setBlock(this.getTargetPos(), newState, 3);
            }

            if (resourceValue == 0) {
                serverLevel.destroyBlock(this.getTargetPos(), false);
                serverLevel.destroyBlockProgress(1, this.getTargetPos(), 0);

                this.setBreakingProgressMilestone(-1);
                this.setHasTarget(false);
                this.setChanged(); //Mark the drill for saving
            }
        }
    }
}