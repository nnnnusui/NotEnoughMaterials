package com.github.nnnnusui.minecraft.nem;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MaterialBlockEntity extends BlockEntity {
    public static final String registryName = MaterialBlock.registryName + "_entity";
    public int lightLevel = 0;

    public MaterialBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockRegister.materialBlockEntity, p_155229_, p_155230_);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("lightLevel", lightLevel);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.lightLevel = tag.getInt("lightLevel");
    }
}
