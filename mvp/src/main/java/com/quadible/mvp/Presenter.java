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

import com.quadible.mvp.annotation.Persistable;

import java.util.ArrayList;
import java.util.UUID;

/**
 * <p>
 *     Base presenter. It implements all the methods that {@link Mvp} needs from a presenter to be
 *     exposed as well as an a mechanism for caching UI actions in order to execute them when UI
 *     becomes visible again.
 * </p>
 */
@Persistable
public abstract class Presenter<U extends UiElement> {

    private UUID mUuid;

    //We are going to cache them manually, because it is possible to have different classes.
    //So we need to keep reference to the class name also.
    private ArrayList<UiAction> mPendingActions = new ArrayList<>();

    private boolean isAttached = false;

    private boolean isRemoved = false;

    private U mUi;

    /**
     * Presenter was restored. Probably the application was killed by the OS. Take an action if
     * needed by implementing this method.
     */
    protected void onRestore() {
        IActionsCache<UiAction> actionsCache = ActionsCacheProvider.newInstance().provide(mUuid);
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
        for (UiAction action : mPendingActions) {
            action.act();
        }

        mPendingActions.clear();
        IActionsCache<UiAction> actionsCache = ActionsCacheProvider.newInstance().provide(mUuid);
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
    protected void post(UiAction action) {
        if (isAttached) {
            action.act();
        } else if (!isRemoved){
            //if presenter is going to be removed we do not want to cache actions

            mPendingActions.add(action);

            //Something changed while the presenter is detached.
            //Keep the new state of the presenter in cache.
            IDataProvider dataProvider = DataProvider.newInstance();
            dataProvider.store(mUuid, this);

            IActionsCache<UiAction> actionsCache =
                    ActionsCacheProvider.newInstance().provide(mUuid);
            actionsCache.saveActions(mPendingActions);
        }
    }

    void setUuid(UUID uuid) {
        mUuid = uuid;
    }

    /**
     * Define an action (or a set of actions) that we want to perform to the UI Element when it is
     * available.
     */
    public interface UiAction {

        /**
         * Perform the UI actions that we want to execute. In order to get the UI element you must
         * call {@link #getUi()}.
         */
        void act();

    }


    /**
     * Get the UI element to which we want to act. The UI element is loaded when the presenter
     * is being attached. Use this method only {@link UiAction}.
     *
     * @return The ui element
     */
    protected final U getUi() {
        return mUi;
    }

}
