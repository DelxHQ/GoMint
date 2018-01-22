package io.gomint.server.world.block;

import io.gomint.world.block.BlockType;

import io.gomint.server.registry.RegisterInfo;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 16 )
public class CoalOre extends Block implements io.gomint.world.block.BlockCoalOre {

    @Override
    public byte getBlockId() {
        return 16;
    }

    @Override
    public long getBreakTime() {
        return 4500;
    }

    @Override
    public float getBlastResistance() {
        return 15.0f;
    }

    @Override
    public BlockType getType() {
        return BlockType.COAL_ORE;
    }

    @Override
    public boolean canBeBrokenWithHand() {
        return true;
    }

}
