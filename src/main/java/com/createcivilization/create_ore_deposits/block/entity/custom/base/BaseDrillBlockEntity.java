package com.createcivilization.create_ore_deposits.block.entity.custom.base;

import com.createcivilization.create_ore_deposits.block.custom.gen.BaseGeneratedDepositOre;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public abstract class BaseDrillBlockEntity extends KineticBlockEntity {
    //TODO:: make kinetic block entity vertical-only cause this is going to be a sort of pulley

    protected int resourcePullSpeed;
    protected int efficiency;
    protected boolean target = false;
    protected BlockPos targetPos = new BlockPos(0, 0, 0);
    private int startTick = 1;

    protected final ItemStackHandler inventory = new ItemStackHandler(1);
    // If for whatever reason the size should be unique, just remove the "1" and make them do .setSize for each instance

    protected double breakingProgressMilestone = -1;
    //TODO -> Make varying speeds affect the drill (ASK ARTY ON THE MATHS FOR THAT)

    public void tick() {
        super.tick();
        assert this.level != null;
        if (this.level.isClientSide()) return;
        if (this.getSpeed() == 0.0F) return;
        ServerLevel serverLevel = (ServerLevel) this.level;
        assert serverLevel != null;
        BlockPos pos = this.getBlockPos();
        if (this.startTick % this.getResourcePullSpeed() != 0) {
            this.startTick++;
            return;
        }
        this.startTick++;

        //Add slot logic if we ever decide we need more than one slot for inventory, you can ask Orion to do this if you want
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

    public double getBreakingProgressMilestone() {
        return this.breakingProgressMilestone;
    }

    public void setBreakingProgressMilestone(double breakingProgressMilestone) {
        this.breakingProgressMilestone = breakingProgressMilestone;
    }

    public BaseDrillBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public BlockPos findFurthestTarget(Level level, BlockPos InitialPos) {
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();
        BlockPos farthestBlock = InitialPos;

        queue.add(InitialPos);
        visited.add(InitialPos);

        while (!queue.isEmpty()) {
            int size = queue.size();
            BlockPos lastInLevel = null;
            for (int i = 0; i < size; i++) {
                BlockPos current = queue.poll();
                lastInLevel = current;

                for (BlockPos neighbor : getPositions(current)) {
                    if (!visited.contains(neighbor) && isBlockDeposit(level, neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }

            farthestBlock = lastInLevel;
        }
        return farthestBlock;
    }

    public static List<BlockPos> getPositions(BlockPos pos) {

        BlockPos above = pos.above();
        BlockPos below = pos.below();
        BlockPos aboveNorth = above.north();
        BlockPos aboveSouth = above.south();
        BlockPos middleNorth = pos.north();
        BlockPos middleSouth = pos.south();
        BlockPos belowNorth = below.north();
        BlockPos belowSouth = below.south();

        return Arrays.asList(
                above, aboveNorth, aboveSouth, above.east(), above.west(), aboveNorth.east(), aboveNorth.west(), aboveSouth.east(), aboveSouth.west(),
                middleNorth, middleSouth, pos.east(), pos.west(), middleNorth.east(), middleNorth.west(), middleSouth.east(), middleSouth.west(),
                below, belowNorth, belowSouth, below.east(), below.west(), belowNorth.east(), belowNorth.west(), belowSouth.east(), belowSouth.west()
        );
    }

    public boolean isBlockDeposit(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getOptionalValue(BaseGeneratedDepositOre.RESOURCE_VALUE).isPresent();
    }

    public int getBreakingProgress(double breakingProgressMilestone, int resourceLevel) {
        double progressRatio = (double) resourceLevel / breakingProgressMilestone;
        int progress = 9 - (int) Math.ceil(progressRatio);
        return Math.min(9, Math.max(1, progress));
    }

    public void setTargetPos(int[] targetPosArray) {
        this.setTargetPos(new BlockPos(targetPosArray[0], targetPosArray[1], targetPosArray[2]));
    }

    public void setTargetPos(BlockPos targetPos) {
        this.targetPos = targetPos;
    }

    public BlockPos getTargetPos() {
        return this.targetPos;
    }

    public void setHasTarget(boolean hasTarget) {
        this.target = hasTarget;
    }

    public boolean hasTarget() {
        return this.target;
    }

    public void setResourcePullSpeed(int resourcePullSpeed) {
        this.resourcePullSpeed = resourcePullSpeed;
    }

    public int getResourcePullSpeed() {
        return this.resourcePullSpeed;
    }

    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }

    public int getEfficiency() {
        return this.efficiency;
    }

    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.setHasTarget(compound.getBoolean("HasTarget"));
        this.setTargetPos(compound.getIntArray("TargetPos"));
        inventory.deserializeNBT(compound.getCompound("inventory"));
    }

    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putBoolean("HasTarget", this.hasTarget());
        compound.putIntArray("TargetPos", new int[]{this.getTargetPos().getX(), this.getTargetPos().getY(), this.getTargetPos().getZ()});
        compound.put("inventory", inventory.serializeNBT());
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return LazyOptional.of(() -> inventory).cast();
        }
        //You can use this to add more capabilities like fluids and etc, add a direction check for side specific stuff
        return super.getCapability(capability, side);
    }
}