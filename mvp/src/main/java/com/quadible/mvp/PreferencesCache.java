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
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * <p>
 *     An implementation of {@link ICache} which uses shared preferences in order to store/restore
 *     {@link Presenter}.
 * </p>
 */
class PreferencesCache implements ICache {

    private static final String PREFERENCE_PRESENTERS_SUFFIX = ".mvpPresenters";

    private static final String PREFERENCE_TYPES_SUFFIX = ".mvpTypes";

    private static PreferencesCache sInstance;

    private final SharedPreferences mPresentersPreferences;

    private final SharedPreferences mTypesPreferences;

    private final String PREFIX_NAME;

    private PreferencesCache(Application application) {
        PREFIX_NAME = application.getPackageName();
        String prefsName = PREFIX_NAME + PREFERENCE_PRESENTERS_SUFFIX;
        mPresentersPreferences = application.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        String typesName = PREFIX_NAME + PREFERENCE_TYPES_SUFFIX;
        mTypesPreferences = application.getSharedPreferences(typesName, Context.MODE_PRIVATE);
    }

    static synchronized PreferencesCache newInstance() {
        if (sInstance == null) {
            throw new RuntimeException(
                    "You must call Mvp.install(Application application) in on create of your Application");
        }
        return sInstance;
    }

    static synchronized PreferencesCache newInstance(Application application) {
        if (sInstance == null) {
            sInstance = new PreferencesCache(application);
        }
        return sInstance;
    }

    /**
     * Get the ids of all presenters that were stored in cache.
     * @return List of cached presenter's ids.
     */
    @Override
    public ArrayList<UUID> getCachedKeys() {
        ArrayList<UUID> actualUuids = new ArrayList<>();

        Set<String> uuids = mPresentersPreferences.getAll().keySet();
        for (String uuid : uuids) {
            UUID actualUuid = UUID.fromString(uuid);
            actualUuids.add(actualUuid);
        }

        return actualUuids;
    }

    /**
     * Store a {@link Presenter} in {@link ICache} using an id (corresponds to presenter's id).
     * @param uuid The id to use.
     * @param presenter The presenter to store
     */
    @Override
    public void add(UUID uuid, Presenter presenter) {
        Check.requireNonNull(uuid);
        Check.requireNonNull(presenter);

        Gson gson = new Gson();
        String serializedPresenter = gson.toJson(presenter);
        String uuidString = uuid.toString();

        mPresentersPreferences.edit()
                .putString(uuidString, serializedPresenter)
                .commit();

        mTypesPreferences.edit()
                .putString(uuidString, presenter.getClass().getName())
                .commit();
    }

    /**
     * Get the {@link Presenter} that was stored in {@link ICache} with the given id.
     * @param uuid The {@link Presenter}'s id.
     * @param type The {@link Presenter}'s class. (Used in order to deserialize it).
     * @param <T> The {@link Presenter}'s type.
     * @return The stored {@link Presenter}.
     */
    @Override
    public <T extends Presenter>Presenter get(UUID uuid, Class<T> type) {
        Check.requireNonNull(uuid);
        Check.requireNonNull(type);

        String uuidString = uuid.toString();
        String serializedPresenter = mPresentersPreferences.getString(uuidString, "{}");
        Gson gson = new Gson();
        T presenter =  gson.fromJson(serializedPresenter, type);
        presenter.onRestore();
        return presenter;
    }

    /**
     * Get {@link Presenter}'s class. Is needed in order to deserialize it.
     * @param uuid The {@link Presenter}'s id
     * @param <T> The expecting {@link Presenter}'s type
     * @return The {@link Presenter}'s class
     */
    @Override
    public <T extends Presenter> Class<T> getType(UUID uuid) {
        Check.requireNonNull(uuid);

        String uuidString = uuid.toString();
        String typeName = mTypesPreferences.getString(uuidString, "");
        Class<T> cls = null;
        try {
            cls = (Class<T>) Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return cls;
    }

    /**
     * Removes a {@link Presenter} from cache (and its corresponding resources, e.g. cached actions
     * - {@link IActionsCache})
     * @param uuid The {@link Presenter}'s id.
     */
    @Override
    public void remove(UUID uuid) {
        Check.requireNonNull(uuid);

        String uuidString = uuid.toString();
        mPresentersPreferences.edit().remove(uuidString).apply();
        mTypesPreferences.edit().remove(uuidString).apply();
        ActionsCacheProvider.newInstance().provide(uuid).delete();
    }

}
