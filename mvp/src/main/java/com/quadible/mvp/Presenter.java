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

import java.util.ArrayList;
import java.util.UUID;

/**
 * <p>
 *     Base presenter. It implements all the methods that {@link Mvp} needs from a presenter to be
 *     exposed as well as an a mechanism for caching UI actions in order to execute them when UI
 *     becomes visible again.
 * </p>
 */
public abstract class Presenter<U extends UiElement> {

    private UUID mUuid;

    //We are going to cache them manually, because it is possible to have different classes.
    //So we need to keep reference to the class name also.
    private ArrayList<UiAction<U>> mPendingActions = new ArrayList<>();

    private boolean isAttached = false;

    private boolean isRemoved = false;

    private U mUi;

    /**
     * Presenter was restored. Probably the application was killed by the OS. Take an action if
     * needed by implementing this method.
     */
    protected void onRestore() {
        IActionsCache<UiAction<U>> actionsCache = ActionsCacheProvider.newInstance().provide(mUuid);
        mPendingActions = actionsCache.restoreActions();
    }

    /**
     * Attach the presenter to the given UI element and execute all the pending UI actions.
     * @param ui The ui element to which we want to attach the presenter
     */
    void attach(U ui) {
        mUi = ui;
        isAttached = true;
        if (mPendingActions.size() > 0) executePendingUiActions();
    }

    /**
     * Set the ui element to the pending UI actions and execute them.
     */
    private void executePendingUiActions() {
        for (UiAction<U> action : mPendingActions) {
            action.setUi(mUi);
            action.act();
        }

        mPendingActions.clear();
        IActionsCache<UiAction<U>> actionsCache = ActionsCacheProvider.newInstance().provide(mUuid);
        actionsCache.delete();
    }

    /**
     * Detach presenter from its UI element.
     */
    void detach() {
        isAttached = false;
        mUi = null;

        //Save the last "visible" instance of presenter
        IDataProvider dataProvider = DataProvider.newInstance();
        dataProvider.store(mUuid, this);
    }

    void setRemoved() {
        isRemoved = true;
    }

    /**
     * Post a {@link UiAction}. If presenter is attached, the action will be executed immediately.
     * In other case the actions will be cached and they will be executed when presenter is attached
     * again.
     * @param action The actions to execute.
     */
    protected void post(UiAction<U> action) {
        if (isAttached) {
            action.setUi(mUi);
            action.act();
        } else if (!isRemoved){
            //if presenter is going to be removed we do not want to cache actions

            mPendingActions.add(action);

            //Something changed while the presenter is detached.
            //Keep the new state of the presenter in cache.
            IDataProvider dataProvider = DataProvider.newInstance();
            dataProvider.store(mUuid, this);

            IActionsCache<UiAction<U>> actionsCache =
                    ActionsCacheProvider.newInstance().provide(mUuid);
            actionsCache.saveActions(mPendingActions);
        }
    }

    void setUuid(UUID uuid) {
        mUuid = uuid;
    }

    /**
     * <p>
     *     Helper class in order to cache UI actions while the presenter is detached.
     * </p>
     * @param <U> The type of UI element to which we want to act.
     */
    public abstract class UiAction<U extends UiElement> {

        private U mUi;

        /**
         * Define the UI actions that we want to execute. In order to get the UI element you must
         * call {@link #getUi()}.
         */
        public abstract void act();

        /**
         * Get the UI element to which we want to act. The ui element is loaded when the presenter
         * is being attached.
         * @return The ui element
         */
        public final U getUi() {
            return mUi;
        }

        /**
         * Used in order to set the UI element to which want to act. It should be called internally
         * only while the presenter is being attached.
         */
        private void setUi(U ui) {
            mUi = ui;
        }

    }

}
