package io.gomint.server.world.block;

import io.gomint.inventory.item.ItemStack;
import io.gomint.world.block.BlockType;

import io.gomint.server.registry.RegisterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( sId = "minecraft:packed_ice" )
public class PackedIce extends Block implements io.gomint.world.block.BlockPackedIce {

    @Override
    public String getBlockId() {
        return "minecraft:packed_ice";
    }

    @Override
    public long getBreakTime() {
        return 750;
    }

    @Override
    public float getBlastResistance() {
        return 2.5f;
    }

    @Override
    public BlockType getType() {
        return BlockType.PACKED_ICE;
    }

    @Override
    public List<ItemStack> getDrops( ItemStack itemInHand ) {
        return new ArrayList<>();
    }

    @Override
    public boolean canBeBrokenWithHand() {
        return true;
    }

}
