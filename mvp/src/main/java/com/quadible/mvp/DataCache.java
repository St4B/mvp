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
import com.quadible.mvp.annotation.DataContainer;

import java.util.UUID;

/**
 * <p>
 *     Implementation {@link IDataCache} that uses the shared preferences as a persistent
 *     storage. It is responsible for restoring/storing {@link Presenter}'s
 *     {@link com.quadible.mvp.annotation.Persistent} fields in preferences.
 * </p>
 */
class DataCache implements IDataCache {

    private static final String PREFERENCE_DATA_SUFFIX = ".mvpData";

    private final String PREFIX_NAME;

    private final SharedPreferences mDataPreferences;

    private final Gson mGson = new Gson();

    DataCache(Application application) {
        PREFIX_NAME = application.getPackageName();
        String prefsName = PREFIX_NAME + PREFERENCE_DATA_SUFFIX;
        mDataPreferences = application.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
    }

    /**
     * Saves the {@link DataContainer} in storage using this uuid. (This uuid the
     * {@link Presenter}'s uuid).
     *
     * @param uuid {@link Presenter}'s uuid
     * @param data The {@link Presenter}'s {@link DataContainer} filled with the values of
     *             presenter's {@link com.quadible.mvp.annotation.Persistent} fields
     */
    @Override
    public void put(UUID uuid, DataContainer data) {
        String uuidString = uuid.toString();
        String serializedData = mGson.toJson(data);
        mDataPreferences.edit().putString(uuidString, serializedData).commit();
    }

    /**
     * Retrieves the {@link DataContainer} corresponding to this uuid. (This uuid the
     * {@link Presenter}'s uuid).
     *
     * @param uuid     {@link Presenter}'s uuid
     * @param type Class of {@link Presenter}'s {@link DataContainer}
     * @param <T>      Type of {@link DataContainer}
     * @return The persisted data of the given {@link Presenter}
     */
    @Override
    public <T extends DataContainer> T get(UUID uuid, Class<T> type) {
        String uuidString = uuid.toString();
        String serializedData = mDataPreferences.getString(uuidString, "{}");
        return mGson.fromJson(serializedData, type);
    }

    /**
     * Removes the {@link DataContainer} corresponding to this uuid. (This uuid the
     * {@link Presenter}'s uuid).
     */
    @Override
    public void remove(UUID uuid) {
        String uuidString = uuid.toString();
        mDataPreferences.edit().remove(uuidString).commit();
    }

    /**
     * Clears the all the {@link DataContainer}s that were stored. This is needed for a clean up at
     * initialization.
     */
    @Override
    public void clear() {
        mDataPreferences.edit().clear().commit();
    }
}
