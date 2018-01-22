package io.gomint.server.world.block;

import io.gomint.world.block.BlockType;

import io.gomint.server.registry.RegisterInfo;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 136 )
public class JungleWoodStairs extends Stairs {

    @Override
    public byte getBlockId() {
        return (byte) 136;
    }

    @Override
    public long getBreakTime() {
        return 3000;
    }

    @Override
    public float getBlastResistance() {
        return 15.0f;
    }

    @Override
    public BlockType getType() {
        return BlockType.JUNGLE_WOOD_STAIRS;
    }

    @Override
    public boolean canBeBrokenWithHand() {
        return true;
    }

}
