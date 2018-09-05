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
 *     Provides a mechanism for persistent storing and restoring the data of {@link Presenter}.
 *     Basically, the actual storing and restoring is delegated to {@link IDataCache}. This class is
 *     responsible for orchestrating the process. Specifically, it extracts the fields that were tag
 *     as {@link com.quadible.mvp.annotation.Persistent} and applies values to them.
 * </p>
 */
interface IDataProvider {

    /**
     * Saves the {@link com.quadible.mvp.annotation.Persistent} data of the given presenter.
     * The uuid is given in order to be able to map the data whenever we call the
     * {@link IDataProvider#restore(UUID, Presenter)}.
     *
     * @param uuid      The {@link Presenter}'s uuid
     * @param presenter The {@link Presenter} from which we want to get the data.
     */
    void store(UUID uuid, Presenter presenter);

    /**
     * Sets the data which corresponds to the given uuid, to the given presenter. (We assume that
     * the uuid is the {@link Presenter}'s uuid).
     *
     * @param uuid The {@link Presenter}'s uuid (it must be the same which was use in
     *             {@link IDataProvider#store(UUID, Presenter)})
     * @param presenter The {@link Presenter} to which we want to set the data.
     * @param <T>  The type of the {@link DataContainer} which we are going to use.
     */
    <T extends DataContainer> void restore(UUID uuid, Presenter presenter);

    /**
     * Removes the stored data corresponding to this uuid. (This uuid the {@link Presenter}'s uuid).
     */
    void remove(UUID uuid);

    /**
     * Clears all the stored data for every @{@link Presenter}). This is needed for a clean up at
     * initialization.
     */
    void clear();

}
