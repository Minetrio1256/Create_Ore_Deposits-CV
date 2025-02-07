package com.createcivilization.create_ore_deposits.block.entity.custom.base;

import com.createcivilization.create_ore_deposits.util.CODTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public abstract class BaseDrillBlockEntity extends BlockEntity {
    public BaseDrillBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
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
                    if(!visited.contains(neighbor) && isBlockDepositMatch(level, neighbor)) {
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

    public static List<BlockPos> getPositions(BlockPos pos){

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

    public static boolean isBlockDepositMatch(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(CODTags.Blocks.ORE_DEPOSITS);
    }

}
