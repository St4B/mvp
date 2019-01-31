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

import android.util.Log;
import java.util.ArrayList;

import com.quadible.mvp.Presenter.UiAction;

/**
 *  <p>
 *      Implementation of ({@link IActionsCache}) that it is going to be used whenever we could
 *      provide an {@link IActionsCache<A>}. I hope it is never going to be created. It here just
 *      in case something really bad happens.
 *  </p>
 */
class NullActionsCache<U extends UiElement> implements IActionsCache<U> {

    private static final String TAG = NullActionsCache.class.getName();

    @Override
    public void saveActions(ArrayList<UiAction<U>> actions) {
        Log.d(TAG, "saveActions() called with: actions = [" + actions + "]");
        throw new RuntimeException("Tried to save with NullActionsCache");
    }

    /**
     * Implementation of {@link IActionsCache#restoreActions()}
     * @return Empty list
     */
    @Override
    public ArrayList<UiAction<U>> restoreActions() {
        Log.d(TAG, "restoreActions() called");
        return new ArrayList<>();
    }

    /**
     * Implementation of {@link IActionsCache#delete()}. Basically it will not do anything.
     */
    @Override
    public void delete() {
        Log.d(TAG, "delete() called");
    }
}
