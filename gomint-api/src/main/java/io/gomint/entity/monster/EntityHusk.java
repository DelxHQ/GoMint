package io.gomint.entity.monster;

import io.gomint.GoMint;
import io.gomint.entity.EntityAgeable;
import io.gomint.entity.EntityLiving;

public interface EntityHusk extends EntityAgeable {

    /**
     * Create a new entity husk with no config
     *
     * @return empty, fresh husk
     */
    static EntityHusk create() {
        return GoMint.instance().createEntity( EntityHusk.class );
    }

}
