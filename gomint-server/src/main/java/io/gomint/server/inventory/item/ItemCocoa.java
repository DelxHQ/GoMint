package io.gomint.server.inventory.item;
import io.gomint.inventory.item.ItemType;

import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 127 )
 public class ItemCocoa extends ItemStack implements io.gomint.inventory.item.ItemCocoa {



    @Override
    public String getBlockId() {
        return "minecraft:cocoa";
    }

    @Override
    public ItemType getType() {
        return ItemType.COCOA;
    }

}
