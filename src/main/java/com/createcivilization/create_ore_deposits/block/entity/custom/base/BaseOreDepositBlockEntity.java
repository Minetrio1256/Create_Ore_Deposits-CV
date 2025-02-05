package com.createcivilization.create_ore_deposits.block.entity.custom.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseOreDepositBlockEntity extends BlockEntity {


    private int resourceLevel = 20;

    public BaseOreDepositBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public abstract ItemStack getExtractionStack(int amount);

    public void setResourceLevel(int resourceLevel) {
        this.resourceLevel = resourceLevel;
    }

    public int getResourceLevel() {
        return resourceLevel;
    }

    @Override
    public void load(CompoundTag pTag) {
        this.setResourceLevel(pTag.getInt("ResourceLevel"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("ResourceLevel", this.getResourceLevel());
    }
}
