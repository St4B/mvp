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

import android.app.Application;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;

import java.util.ArrayList;
import java.util.UUID;

/**
 * <p>
 *     A singleton that is responsible for storing/restoring presenters while the UI elements are
 *     being recreated. So, we can attach the presenters to the new instances of the UI elements.
 *     The presenters are uniquely identified based on a UUID. Also, it is responsible for
 *     restoring the presenters after the app was killed. The action each {@link Presenter} is going
 *     to take on restore is defined in {@link Presenter#onRestore()}.
 * </p>
 */
class PresenterProvider implements IPresenterProvider{

    private ArrayMap<UUID, Presenter> mPresenters = new ArrayMap<>();

    private ArrayMap<UUID, Class> mPresenterTypes = new ArrayMap<>();

    private ICache mCache;

    private static PresenterProvider sInstance;

    private PresenterProvider(Application application) {
        mCache = PreferencesCache.newInstance(application);
    }

    /**
     * Get the PresenterProvider.
     * @return The PresenterProvider singleton object.
     */
    static PresenterProvider newInstance() {
        if (sInstance == null) {
            throw new RuntimeException(
                    "You must call Mvp.install(Application application) in on create of your Application");
        }
        return sInstance;
    }

    static void init(Application application) {
        if (sInstance == null) sInstance = new PresenterProvider(application);
    }

    /**
     * Check if there are presenters in cache and try to restore them. For example, there would be
     * presenters in cache if application was killed by the OS.
     */
    void restoreIfNeeded() {
        //Get all keys
        ArrayList<UUID> uuids = mCache.getCachedKeys();
        for (UUID uuid : uuids) {
            //Get presenter's type in order to re create it
            Class<? extends Presenter> type = mCache.getType(uuid);

            //Restore presenter
            Presenter presenter = mCache.get(uuid, type);

            //Keep restored data in order to use it
            mPresenterTypes.put(uuid, type);
            mPresenters.put(uuid, presenter);
        }
    }

    /**
     * Store a presenter based on a unique identifier. Later the corresponding ui element can
     * restore the presenter based on this unique identifier.
     * @param uuid The unique identifier.
     * @param presenter The presenter.
     * @param <P> The type of the presenter.
     */
    @Override
    public <P extends Presenter> void add(UUID uuid, P presenter) {
        Check.requireNonNull(uuid);
        Check.requireNonNull(presenter);
        Check.requireNotExist(uuid, mPresenters);

        mPresenters.put(uuid, presenter);
        mPresenterTypes.put(uuid, presenter.getClass());
        mCache.add(uuid, presenter);
    }

    /**
     * Get a stored presenter based on the unique identifier.
     * @param uuid The unique identifier.
     * @param <P> The type of the presenter.
     * @return The presenter as <P> type object
     */
    @Override
    public <P extends Presenter> P get(UUID uuid) {
        Check.requireNonNull(uuid);
        Class<P> type = mPresenterTypes.get(uuid);
        return type.cast(mPresenters.get(uuid));
    }

    /**
     * Remove the {@link Presenter} which corresponds to the given uuid.
     * @param uuid The unique identifier.
     */
    @Override
    public void remove(UUID uuid) {
        Check.requireNonNull(uuid);
        mPresenterTypes.remove(uuid);
        Presenter presenter = mPresenters.get(uuid);

        if (presenter != null) {
            presenter.setRemoved();
            mPresenters.remove(uuid);
            mCache.remove(uuid);
        }
    }

    /**
     * Clear all the presenters. Namely the presenters that were created in our current session as
     * well as the presenters that were stored in previous sessions and never been restored (possible
     * after crash).
     */
    @Override
    public void clear() {
        mCache.clear();
    }

}
