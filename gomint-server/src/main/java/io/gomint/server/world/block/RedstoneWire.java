package io.gomint.server.world.block;

import io.gomint.world.block.BlockType;

import io.gomint.server.registry.RegisterInfo;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( sId = "minecraft:redstone_wire" )
public class RedstoneWire extends Block implements io.gomint.world.block.BlockRedstoneWire {

    @Override
    public String getBlockId() {
        return "minecraft:redstone_wire";
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public float getBlastResistance() {
        return 0.0f;
    }

    @Override
    public BlockType getType() {
        return BlockType.REDSTONE_WIRE;
    }

    @Override
    public boolean canBeBrokenWithHand() {
        return true;
    }

}
