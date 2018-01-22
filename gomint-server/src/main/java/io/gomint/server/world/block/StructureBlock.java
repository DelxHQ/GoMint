package io.gomint.server.world.block;

import io.gomint.world.block.BlockType;

import io.gomint.server.registry.RegisterInfo;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 252 )
public class StructureBlock extends Block implements io.gomint.world.block.BlockStructureBlock {

    @Override
    public byte getBlockId() {
        return (byte) 252;
    }

    @Override
    public float getBlastResistance() {
        return 1.8E7f;
    }

    @Override
    public BlockType getType() {
        return BlockType.STRUCTURE_BLOCK;
    }

    @Override
    public boolean canBeBrokenWithHand() {
        return true;
    }

}
