package com.github.nnnnusui.minecraft.nem;

import com.github.nnnnusui.minecraft.StringProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;

public class MaterialBlock extends Block {
    static final String registryName = "material";
    static final String propertyName = "model";
    private static final IProperty<String> modelProperty = StringProperty.create(propertyName, ResourceGenerator.getStateList());

    MaterialBlock(){
        super(Block.Properties.create(Material.PLANTS));
        setRegistryName(NotEnoughMaterials.modId, registryName);
    }
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(MaterialBlock.modelProperty);
    }
}
