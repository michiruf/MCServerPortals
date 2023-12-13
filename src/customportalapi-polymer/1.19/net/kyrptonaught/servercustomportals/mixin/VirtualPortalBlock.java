package net.kyrptonaught.servercustomportals.mixin;

import eu.pb4.polymer.api.block.PolymerBlock;
import eu.pb4.polymer.api.block.PolymerBlockUtils;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.networking.ForcePlacePortalPacket;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;

// See https://github.com/kyrptonaught/CustomPortalApi-Polymer/blob/1.18/src/main/java/net/kyrptonaught/servercustomportals/mixin/VirtualPortalBlock.java
@Mixin(CustomPortalBlock.class)
public class VirtualPortalBlock implements PolymerBlock {

    @Override
    public Block getPolymerBlock(BlockState state) {
        Direction.Axis dir = CustomPortalHelper.getAxisFrom(state);
        if (dir == Direction.Axis.Y)
            return Blocks.END_PORTAL;
        return Blocks.NETHER_PORTAL;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        Direction.Axis dir = CustomPortalHelper.getAxisFrom(state);
        if (dir == Direction.Axis.Y)
            return Blocks.END_PORTAL.getDefaultState();

        if (dir == Direction.Axis.Z)
            return this.getPolymerBlock(state).getDefaultState().with(NetherPortalBlock.AXIS, Direction.Axis.Z);

        return this.getPolymerBlock(state).getDefaultState();
    }

    @Override
    public void onPolymerBlockSend(ServerPlayerEntity player, BlockPos.Mutable pos, BlockState blockState) {
        Direction.Axis dir = CustomPortalHelper.getAxisFrom(blockState);
        if (dir == Direction.Axis.Y) {
            player.networkHandler.sendPacket(PolymerBlockUtils.createBlockEntityPacket(pos, BlockEntityType.END_PORTAL, new NbtCompound()));
        }
        ForcePlacePortalPacket.sendForcePacket(player, pos.toImmutable());
    }
}