package io.gomint.server.world.block;

import io.gomint.world.block.BlockBirchDoor;
import io.gomint.world.block.BlockType;

import io.gomint.server.registry.RegisterInfo;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 194 )
public class BirchDoor extends Door implements BlockBirchDoor {

    @Override
    public byte getBlockId() {
        return (byte) 194;
    }

    @Override
    public float getBlastResistance() {
        return 15.0f;
    }

    @Override
    public BlockType getType() {
        return BlockType.BIRCH_DOOR;
    }

    @Override
    public boolean canBeBrokenWithHand() {
        return true;
    }
}
