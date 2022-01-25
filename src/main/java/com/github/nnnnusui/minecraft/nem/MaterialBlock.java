package com.github.nnnnusui.minecraft.nem;

import com.github.nnnnusui.minecraft.StringProperty;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;

public class MaterialBlock extends Block {
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
}
