package io.gomint.server.inventory.item;
import io.gomint.inventory.item.ItemType;

import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 174 )
 public class ItemPackedIce extends ItemStack implements io.gomint.inventory.item.ItemPackedIce {



    @Override
    public String getBlockId() {
        return "minecraft:packed_ice";
    }

    @Override
    public ItemType getType() {
        return ItemType.PACKED_ICE;
    }

}
