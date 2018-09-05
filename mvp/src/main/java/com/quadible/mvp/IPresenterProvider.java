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

import java.util.UUID;

/**
 * <p>
 *     IPresenterProvider is responsible for storing/restoring presenters while the UI elements are
 *     being recreated. So, we can attach the presenters to the new instances of the UI elements.
 *     The presenters are uniquely identified based on a UUID.
 * </p>
 */
interface IPresenterProvider {

    /**
     * Store a presenter based on a unique identifier. Later the corresponding ui element can
     * restore the presenter based on this unique identifier.
     * @param uuid The unique identifier.
     * @param presenter The presenter.
     * @param <P> The type of the presenter.
     */
    <P extends Presenter> void add(UUID uuid, P presenter);

    /**
     * Get a stored presenter based on the unique identifier.
     * @param uuid The unique identifier.
     * @param <P> The type of the presenter.
     * @return The presenter as <P> type object
     */
    <P extends Presenter> P get(UUID uuid);

    /**
     * Remove the {@link Presenter} which corresponds to the given uuid.
     * @param uuid The unique identifier.
     */
    void remove(UUID uuid);

}
