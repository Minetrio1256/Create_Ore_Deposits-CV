package com.createcivilization.create_ore_deposits.block.entity.custom.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseOreDepositBlockEntity extends BlockEntity {

    private int resourceLevel;

    public BaseOreDepositBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public ItemStack getExtractionStack(int amount) {
        if (this.getResourceLevel() < amount) amount = this.getResourceLevel();
        this.setResourceLevel(this.getResourceLevel() - amount);
        return this.getExtractionStack0(amount);
    }

    public ItemStack getExtractionStack0(int amount) {
        return new ItemStack(this::getExtractionItem, amount);
    }

    public abstract Item getExtractionItem();

    public void setResourceLevel(int resourceLevel) {
        this.resourceLevel = resourceLevel;
    }

    public int getResourceLevel() {
        return this.resourceLevel;
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