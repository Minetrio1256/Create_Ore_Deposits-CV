package com.createcivilization.create_ore_deposits.block.entity.custom.base;

import com.createcivilization.create_ore_deposits.tag.CODTags;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public abstract class BaseDrillBlockEntity extends BlockEntity {

    protected int resourcePullSpeed;
    protected int efficiency;
    protected boolean target = false;
    protected BlockPos targetPos = new BlockPos(0, 0, 0);

    protected double breakingProgressMilestone = -1;

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

    @Override
    public void load(CompoundTag pTag) {
        this.setHasTarget(pTag.getBoolean("HasTarget"));
        this.setTargetPos(pTag.getIntArray("TargetPos"));
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        pTag.putBoolean("HasTarget", this.hasTarget());
        pTag.putIntArray("TargetPos", new int[]{this.getTargetPos().getX(), this.getTargetPos().getY(), this.getTargetPos().getZ()});
    }
}