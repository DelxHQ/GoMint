package io.gomint.server.inventory.item;
import io.gomint.inventory.item.ItemType;

import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 24 )
 public class ItemSandstone extends ItemStack implements io.gomint.inventory.item.ItemSandstone {



    @Override
    public String getBlockId() {
        return "minecraft:sandstone";
    }

    @Override
    public ItemType getType() {
        return ItemType.SANDSTONE;
    }

}
