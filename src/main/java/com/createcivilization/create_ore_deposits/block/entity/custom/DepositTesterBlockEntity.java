package com.createcivilization.create_ore_deposits.block.entity.custom;

import com.createcivilization.create_ore_deposits.block.CODBlocks;
import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.block.entity.custom.base.BaseOreDepositBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class DepositTesterBlockEntity extends BlockEntity {

    private boolean target = false;
    private BlockPos targetPos = new BlockPos(0, 0, 0);

    public DepositTesterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CODBlockEntities.DEPOSIT_TESTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public void tick(Level Level, BlockPos Pos, BlockState State) {
        if(level instanceof ServerLevel serverLevel) {
            BlockPos below = new BlockPos(Pos.getX(), Pos.getY() - 1, Pos.getZ());
            if(!this.target && (serverLevel.getBlockState(below).getBlock() == CODBlocks.IRON_ORE_DEPOSIT_BLOCK.get())) {
                BlockPos furthestBlock = findFurthestTarget(Level, below);
                this.target = true;
                this.targetPos = furthestBlock;
                System.out.println("Furthest Target Block FOUND: " + furthestBlock);
            }



            if(target) {
                if(!(serverLevel.getBlockState(below).getBlock() == CODBlocks.IRON_ORE_DEPOSIT_BLOCK.get())) {
                    this.target = false;
                    return;
                }
                System.out.println("Attempting To Fuck With Target Block");
                var blockEntity = serverLevel.getBlockEntity(this.targetPos);
                if(blockEntity instanceof BaseOreDepositBlockEntity BE) {
                    System.out.println("Resource Level: "+ BE.getResourceLevel());
                    if(BE.getResourceLevel() == 0){
                        level.setBlock(this.targetPos, Blocks.AIR.defaultBlockState(), 3);
                        target = false;
                    } else if(BE.getResourceLevel() > 0){
                        BE.setResourceLevel(BE.getResourceLevel() - 1);
                        System.out.println("Resource Level NEW: "+BE.getResourceLevel());
                    }

                }
            }

        }



    }

    public static BlockPos findFurthestTarget(Level level, BlockPos InitialPos) {
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();
        BlockPos farthestBlock = InitialPos;

        queue.add(InitialPos);
        visited.add(InitialPos);

        while(!queue.isEmpty()) {
            int size = queue.size();
            BlockPos lastInLevel = null;
            for(int i = 0; i < size; i++) {
                BlockPos current = queue.poll();
                lastInLevel = current;

                for(BlockPos neighbor : getPositions(current)){
                    if(!visited.contains(neighbor) && isBlockMatch(level, neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }

            if(lastInLevel != null) {
                farthestBlock = lastInLevel;
            }
        }
        return farthestBlock;
    }

    private static boolean isBlockMatch(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() == CODBlocks.IRON_ORE_DEPOSIT_BLOCK.get();
    }

    private static List<BlockPos> getPositions(BlockPos pos){

        BlockPos above = pos.above();
        BlockPos below = pos.below();
        BlockPos aboveNorth = above.north();
        BlockPos aboveSouth = above.south();
        BlockPos middleNorth = pos.north();
        BlockPos middleSouth = pos.south();
        BlockPos belowNorth = below.north();
        BlockPos belowSouth = below.south();

        return Arrays.asList(
                above, aboveNorth, aboveSouth, above.east(), above.west(),aboveNorth.east(), aboveNorth.west(), aboveSouth.east(),aboveSouth.west(),
                pos.east(),pos.west(),middleSouth,middleNorth,middleNorth.east(),middleNorth.west(),middleSouth.east(),middleSouth.west(),
                below, belowNorth, belowSouth, below.east(), below.west(), belowNorth.east(),belowNorth.west(),belowSouth.east(),belowSouth.west()
        );
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
