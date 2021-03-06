package io.gomint.server.inventory.item;
import io.gomint.inventory.item.ItemType;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 89 )
public class ItemGlowstone extends ItemStack implements io.gomint.inventory.item.ItemGlowstone {



    @Override
    public String getBlockId() {
        return "minecraft:glowstone";
    }

    @Override
    public ItemType getType() {
        return ItemType.GLOWSTONE;
    }

}
