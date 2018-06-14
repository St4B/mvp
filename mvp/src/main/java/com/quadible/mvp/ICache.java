/**
 * Copyright 2017 Quadible Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quadible.mvp;

import java.util.ArrayList;
import java.util.UUID;

/**
 * <p>
 *     It is responsible for restoring/storing a {@link Presenter}.
 * </p>
 */
interface ICache {

    /**
     * Get the ids of all presenters that were stored in cache.
     * @return List of cached presenter's ids.
     */
    ArrayList<UUID> getCachedKeys();

    /**
     * Store a {@link Presenter} in {@link ICache} using an id (corresponds to presenter's id).
     * @param uuid The id to use.
     * @param presenter The presenter to store
     */
    void add(UUID uuid, Presenter presenter);

    /**
     * Get the {@link Presenter} that was stored in {@link ICache} with the given id.
     * @param uuid The {@link Presenter}'s id.
     * @param type The {@link Presenter}'s class. (Used in order to deserialize it).
     * @param <T> The {@link Presenter}'s type.
     * @return The stored {@link Presenter}.
     */
    <T extends Presenter>Presenter get(UUID uuid, Class<T> type);

    /**
     * Get {@link Presenter}'s class. Is needed in order to deserialize it.
     * @param uuid The {@link Presenter}'s id
     * @param <T> The expecting {@link Presenter}'s type
     * @return The {@link Presenter}'s class
     */
    <T extends Presenter> Class<T> getType(UUID uuid);

    /**
     * Removes a {@link Presenter} from cache (and its corresponding resources, e.g. cached actions
     * - {@link IActionsCache})
     * @param uuid The {@link Presenter}'s id.
     */
    void remove(UUID uuid);

    /**
     * Clear the cache
     */
    void clear();
}
