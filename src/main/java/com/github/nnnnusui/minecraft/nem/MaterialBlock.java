package com.github.nnnnusui.minecraft.nem;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class MaterialBlock extends Block {
    static final String registryName = "material";
    static final String propertyName = "model";

    MaterialBlock(){
        super(Block.Properties.create(Material.PLANTS));
        setRegistryName(NotEnoughMaterials.modId, registryName);
    }
}
