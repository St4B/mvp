package com.quadible.mvp;

import java.util.UUID;

/**
 * <p>
 *     IPresenterProvider is responsible for storing/restoring presenters while the UI elements are
 *     being recreated. So, we can attach the presenters to the new instances of the UI elements.
 *     The presenters are uniquely identified based on a UUID.
 * </p>
 * Created by v.tsitsonis on 11/10/2017.
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

    void clear();

}
