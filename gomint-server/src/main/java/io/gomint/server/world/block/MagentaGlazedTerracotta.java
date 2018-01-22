package io.gomint.server.world.block;

import io.gomint.world.block.BlockType;

import io.gomint.server.registry.RegisterInfo;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 222 )
public class MagentaGlazedTerracotta extends Block implements io.gomint.world.block.BlockMagentaGlazedTerracotta {

    @Override
    public byte getBlockId() {
        return (byte) 222;
    }

    @Override
    public long getBreakTime() {
        return 2100;
    }

    @Override
    public float getBlastResistance() {
        return 7.0f;
    }

    @Override
    public BlockType getType() {
        return BlockType.MAGENTA_GLAZED_TERRACOTTA;
    }

    @Override
    public boolean canBeBrokenWithHand() {
        return true;
    }

}
