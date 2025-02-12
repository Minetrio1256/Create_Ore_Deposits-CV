package com.createcivilization.create_ore_deposits.block.entity.custom.base;

import com.createcivilization.create_ore_deposits.tag.CODTags;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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

    protected int resourcePullSpeed;
    protected int efficiency;
    protected boolean target = false;
    protected BlockPos targetPos = new BlockPos(0, 0, 0);

    protected final ItemStackHandler inventory = new ItemStackHandler(1);
    // If for whatever reason the size should be unique, just remove the "1" and make them do .setSize for each instance

    protected double breakingProgressMilestone = -1;

    public abstract void onTick();

    public void tick() {
        super.tick();
        assert this.level != null;
        if (this.level.isClientSide()) return;
        if (this.getSpeed() == 0.0F) return;
        onTick();
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
        return state.is(CODTags.Blocks.ORE_DEPOSITS);
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