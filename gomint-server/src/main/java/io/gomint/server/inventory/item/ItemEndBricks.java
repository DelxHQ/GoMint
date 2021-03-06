package io.gomint.server.inventory.item;
import io.gomint.inventory.item.ItemType;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 206 )
public class ItemEndBricks extends ItemStack implements io.gomint.inventory.item.ItemEndBricks {



    @Override
    public String getBlockId() {
        return "minecraft:end_bricks";
    }

    @Override
    public ItemType getType() {
        return ItemType.END_BRICKS;
    }

}
