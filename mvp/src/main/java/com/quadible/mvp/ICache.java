package com.quadible.mvp;

import java.util.ArrayList;
import java.util.UUID;

/**
 * <p>
        fixme
 * </p>
 * Created by v.tsitsonis on 14/12/2017.
 */

interface ICache {

    /**
     * fixme
     * @return
     */
    ArrayList<UUID> getCachedKeys();

    /**
     * fixme
     * @param uuid
     * @param presenter
     */
    void cache(UUID uuid, Presenter presenter);

    /**
     * fixme
     * @param uuid
     * @param type
     * @param <T>
     * @return
     */
    <T extends Presenter>Presenter get(UUID uuid, Class<T> type);

    /**
     * fixme
     * @param uuid
     * @param <T>
     * @return
     */
    <T extends Presenter> Class<T> getType(UUID uuid);

    /**
     * fixme
     * @param uuid
     */
    void clear(UUID uuid);

}
