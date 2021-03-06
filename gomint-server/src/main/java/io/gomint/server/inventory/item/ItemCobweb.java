package io.gomint.server.inventory.item;
import io.gomint.inventory.item.ItemType;

import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 30 )
 public class ItemCobweb extends ItemStack implements io.gomint.inventory.item.ItemCobweb {



    @Override
    public String getBlockId() {
        return "minecraft:web";
    }

    @Override
    public ItemType getType() {
        return ItemType.COBWEB;
    }

}
