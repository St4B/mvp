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

import com.quadible.mvp.Presenter.UiAction;

import java.util.UUID;

/**
 * <p>
 *     This class is responsible for providing the corresponding {@link IActionsCache}
 *     implementation of each {@link Presenter}. Each {@link Presenter} has its own
 *     {@link IActionsCache}.
 * </p>
 */
class ActionsCacheProvider {

    private final Application mApplication;

    private static ActionsCacheProvider sInstance;

    private ActionsCacheProvider(Application application) {
        mApplication = application;
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
    <A extends UiAction> IActionsCache<A> provide(UUID uuid) {
        if (uuid == null) {
            return new NullActionsCache<>();
        } else {
            return new ActionsCache<>(mApplication, uuid);
        }
    }

}
