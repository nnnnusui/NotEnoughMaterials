package com.github.nnnnusui.minecraft.nem;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    // Synchronizing on LevelChunk Load
    // https://mcforge.readthedocs.io/en/1.18.x/blockentities/blockentity/#synchronizing-on-levelchunk-load
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }
    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        CompoundTag tag = packet.getTag();
        if (tag == null) return;
        this.load(tag);
    }
}
