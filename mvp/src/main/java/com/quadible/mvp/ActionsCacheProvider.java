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

import com.quadible.mvp.Presenter.UiAction;

import java.util.Set;
import java.util.UUID;

/**
 * <p>
 *     This class is responsible for providing the corresponding {@link IActionsCache}
 *     implementation of each {@link Presenter}. Each {@link Presenter} has its own
 *     {@link IActionsCache}. Also, this class keeps track of provided {@link IActionsCache} in
 *     order to be able to perform clean up at initialization.
 * </p>
 */
class ActionsCacheProvider {

    private static final String PREFERENCE_ACTIONS_INDEX_SUFFIX = ".mvpActionsIndex";

    private final SharedPreferences mActionsIndexPreferences;

    private final Application mApplication;

    private static ActionsCacheProvider sInstance;

    private ActionsCacheProvider(Application application) {
        mApplication = application;
        String prefsName = application.getPackageName() + PREFERENCE_ACTIONS_INDEX_SUFFIX;
        mActionsIndexPreferences = application.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
    }

    static ActionsCacheProvider newInstance() {
        if (sInstance == null) {
            throw new RuntimeException(
                    "You must call Mvp.install(Application application) in on create of your Application");
        }
        return sInstance;
    }

    static void init(Application application) {
        if (sInstance == null) sInstance = new ActionsCacheProvider(application);
    }

    /**
     * Provide the {@link IActionsCache<A>} for the {@link Presenter} with the specified id.
     * @param uuid The {@link Presenter}'s id.
     * @param <A> Type of {@link Presenter}'s {@link UiAction}.
     * @return The cache!
     */
    <U extends UiElement> IActionsCache<U> provide(UUID uuid) {
        if (uuid == null) {
            return new NullActionsCache<>();
        } else {
            String uuidString = uuid.toString();
            mActionsIndexPreferences.edit()
                    .putString(uuidString, uuidString)
                    .apply();
            return new ActionsCache<>(mApplication, uuid);
        }
    }

    /**
     * Clears cached actions for every presenter that was stored. (Used for clean up)
     */
    void clear() {
        Set<String> caches = mActionsIndexPreferences.getAll().keySet();

        for (String key : caches) {
            UUID uuid = UUID.fromString(key);
            provide(uuid).delete();
            mActionsIndexPreferences.edit()
                    .remove(key)
                    .apply();
        }

    }

}
