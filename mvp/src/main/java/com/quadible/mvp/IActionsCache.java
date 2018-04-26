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

import com.quadible.mvp.Presenter.UiAction;

import java.util.ArrayList;

/**
 * <p>
 *     It is responsible for restoring/storing {@link Presenter}'s pending actions ({@link UiAction})
 *     in a persistent storage. Pending actions can occurred while the app is in the background.
 *     The logic is to have one {@link IActionsCache} per {@link Presenter}.
 * </p>
 */
interface IActionsCache<A extends UiAction> {

    /**
     * Save {@link Presenter}'s pending actions in a persistent storage.
     * @param actions Actions that we want to save.
     */
    void saveActions(ArrayList<A> actions);

    /**
     * Return pending actions from the persistent storage. Used after {@link Presenter} was restored.
     * @return The pending actions that were stored.
     */
    ArrayList<A> restoreActions();

    /**
     * Delete actions from persistent storage.
     */
    void delete();

}
