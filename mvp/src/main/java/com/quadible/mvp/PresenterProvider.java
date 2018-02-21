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
import android.support.v4.util.SimpleArrayMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

/**
 * <p>
 *     A singleton that is responsible for storing/restoring presenters while the UI elements are
 *     being recreated. So, we can attach the presenters to the new instances of the UI elements.
 *     The presenters are uniquely identified based on a UUID. Also, it is responsible for
 *     restoring the presenters after the app was killed.
 * </p>
 */
class PresenterProvider implements IPresenterProvider{

    private SimpleArrayMap<UUID, Presenter> mPresenters = new SimpleArrayMap<>();

    private SimpleArrayMap<UUID, Class> mPresenterTypes = new SimpleArrayMap<>();

    private ICache mCache;

    private static PresenterProvider sInstance;

    private PresenterProvider(Application application) {
        mCache = PreferencesCache.newInstance(application);
        restoreIfNeeded();
    }

    /**
     * Get the PresenterProvider.
     * @return The PresenterProvider singleton object.
     */
    protected static PresenterProvider newInstance() {
        if (sInstance == null) {
            throw new RuntimeException(
                    "You must call Mvp.install(Application application) in on create of your Application");
        }
        return sInstance;
    }


    public static void init(Application application) {
        if (sInstance == null) sInstance = new PresenterProvider(application);
    }

    /**
     * Check if there are presenters in cache and try to restore them. For example, there would be
     * presenters in cache if application was killed by the system
     */
    private void restoreIfNeeded() {
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
    public <P extends Presenter> void add(UUID uuid, P presenter) {
        if (mPresenters.containsKey(uuid)) {} //FIXME
        //if presenter == null fixme

        mPresenters.put(uuid, presenter);
        mPresenterTypes.put(uuid, presenter.getClass());
        mCache.cache(uuid, presenter);
    }

    /**
     * Get a stored presenter based on the unique identifier.
     * @param uuid The unique identifier.
     * @param <P> The type of the presenter.
     * @return The presenter as <P> type object
     */
    public <P extends Presenter> P get(UUID uuid) {
        Class<P> type = mPresenterTypes.get(uuid);
        return type.cast(mPresenters.get(uuid));
    }

    /**
     * Remove the {@link Presenter} which corresponds to the given uuid.
     * @param uuid The unique identifier.
     */
    public void remove(UUID uuid) {
        mPresenterTypes.remove(uuid);
        mPresenters.remove(uuid);
        mCache.clear(uuid);
    }

}
