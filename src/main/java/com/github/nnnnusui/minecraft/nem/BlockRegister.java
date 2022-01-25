package com.github.nnnnusui.minecraft.nem;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = NotEnoughMaterials.modId, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(NotEnoughMaterials.modId)
public class BlockRegister {
    private static final MaterialBlock materialBlock = new MaterialBlock();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
                new BlockItem(materialBlock, new Item.Properties()).setRegistryName(NotEnoughMaterials.modId, MaterialBlock.registryName)
        );
    }
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        event.getRegistry().registerAll(
                materialBlock
        );
    }
}
