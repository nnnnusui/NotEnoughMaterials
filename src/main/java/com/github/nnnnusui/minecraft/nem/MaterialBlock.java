package com.github.nnnnusui.minecraft.nem;

import com.github.nnnnusui.minecraft.StringProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MaterialBlock extends Block implements EntityBlock {
    public static final String registryName = "material";
    public static final String propertyName = "model";
    private static final Property<String> modelProperty = StringProperty.create(propertyName, ResourceGenerator.getStateList());

    MaterialBlock(){
        super(BlockBehaviour.Properties.of(Material.PLANT));
        setRegistryName(NotEnoughMaterials.modId, registryName);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MaterialBlock.modelProperty);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlockRegister.materialBlockEntity.create(pos, state);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return world.getBlockEntity(pos, BlockRegister.materialBlockEntity)
            .map(it -> it.lightLevel)
            .orElse(super.getLightEmission(state, world, pos));
    }
}
