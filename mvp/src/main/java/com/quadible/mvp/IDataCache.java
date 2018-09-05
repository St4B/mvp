/**
 * Copyright 2017 Quadible Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quadible.mvp;

import com.quadible.mvp.annotation.DataContainer;

import java.util.UUID;

/**
 * <p>
 *     It is responsible for restoring/storing {@link Presenter}'s
 *     {@link com.quadible.mvp.annotation.Persistent} fields in a persistent storage.
 * </p>
 */
interface IDataCache {

    /**
     * Saves the {@link DataContainer} in storage using this uuid. (This uuid the
     * {@link Presenter}'s uuid).
     *
     * @param uuid {@link Presenter}'s uuid
     * @param data The {@link Presenter}'s {@link DataContainer} filled with the values of
     *             presenter's {@link com.quadible.mvp.annotation.Persistent} fields
     */
    void put(UUID uuid, DataContainer data);

    /**
     * Retrieves the {@link DataContainer} corresponding to this uuid. (This uuid the
     * {@link Presenter}'s uuid).
     *
     * @param uuid     {@link Presenter}'s uuid
     * @param classOfT Class of {@link Presenter}'s {@link DataContainer}
     * @param <T>      Type of {@link DataContainer}
     * @return The persisted data of the given {@link Presenter}
     */
    <T extends DataContainer> T get(UUID uuid, Class<T> classOfT);

    /**
     * Removes the {@link DataContainer} corresponding to this uuid. (This uuid the
     * {@link Presenter}'s uuid).
     */
    void remove(UUID uuid);

    /**
     * Clears the all the {@link DataContainer}s that were stored. This is needed for a clean up at
     * initialization.
     */
    void clear();

}
