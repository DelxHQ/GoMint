package io.gomint.server.world.block;

import io.gomint.world.block.BlockType;

import io.gomint.server.registry.RegisterInfo;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 23 )
public class Dispenser extends Block implements io.gomint.world.block.BlockDispenser {

    @Override
    public byte getBlockId() {
        return 23;
    }

    @Override
    public long getBreakTime() {
        return 5250;
    }

    @Override
    public float getBlastResistance() {
        return 1.0f;
    }

    @Override
    public BlockType getType() {
        return BlockType.DISPENSER;
    }

    @Override
    public boolean canBeBrokenWithHand() {
        return true;
    }

}
