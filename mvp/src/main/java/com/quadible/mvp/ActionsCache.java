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

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * <p>
 *     Implementation of ({@link IActionsCache}) that uses the shared preferences as a persistent
 *     storage.
 *     <br/>
 *     It creates a different shared preferences file per {@link Presenter} using it's id in order
 *     to distinguish them.
 *     <br/>
 *     The logic here is to save the actions using as key the position of the {@link UiAction} inside
 *     the pending action's list. This helps us to avoid the overhead of continuous serializing/
 *     deserializing when we had a new pending action, if we saved the list at a whole.
 * </p>
 */
class ActionsCache<U extends UiElement> implements IActionsCache<U> {

    private static final String PREFERENCE_ACTIONS_SUFFIX = ".mvpActions";

    private static final String KEY_ACTIONS_TYPES = "types";

    private static final String ACTIONS_SEPARATOR = ",";

    private static final String ACTIONS_TYPES_EMPTY_VALUE = "eimaiToEmptyValue";

    private final SharedPreferences mActionsPreferences;

    private final Application mApplication;

    private final String mFileName;

    ActionsCache(Application application, UUID uuid) {
        mApplication = application;
        mFileName = uuid.toString() + PREFERENCE_ACTIONS_SUFFIX;
        mActionsPreferences = mApplication.getSharedPreferences(mFileName, Context.MODE_PRIVATE);
    }

    /**
     * Save {@link Presenter}'s pending actions in a persistent storage.
     * @param actions Actions that we want to save.
     */
    @Override
    public void saveActions(ArrayList<UiAction<U>> actions) {
        if (actions == null || actions.size() == 0) return;

        StringBuilder actionTypes = new StringBuilder();

        for (int i = 0, size = actions.size(); i < size; i++) {
            Presenter.UiAction action = actions.get(i);
            actionTypes.append(action.getClass().getName());
            actionTypes.append(ACTIONS_SEPARATOR);
        }

        //Remove last separator
        actionTypes.setLength(actionTypes.length() - ACTIONS_SEPARATOR.length());

        mActionsPreferences.edit().putString(KEY_ACTIONS_TYPES, actionTypes.toString()).commit();
    }

    /**
     * Return pending actions from the persistent storage. Used after {@link Presenter} was restored.
     * @return The pending actions that were stored.
     */
    @Override
    public ArrayList<UiAction<U>> restoreActions() {
        String typesInString =
                mActionsPreferences.getString(KEY_ACTIONS_TYPES, ACTIONS_TYPES_EMPTY_VALUE);

        if (ACTIONS_TYPES_EMPTY_VALUE.equals(typesInString)) {
            return new ArrayList<>();
        }

        String[] types = typesInString.split(ACTIONS_SEPARATOR);

        ArrayList<UiAction<U>> actions = new ArrayList<>();

        for (int i = 0; i < types.length; i++) {
            String typeName = types[i];
            Class<? extends Presenter.UiAction> cls;
            try {

                cls = (Class<UiAction<U>>) Class.forName(typeName);
                Constructor<?> constructor = cls.getDeclaredConstructors()[0];
                boolean accessible = constructor.isAccessible();
                constructor.setAccessible(true);
                UiAction<U> action = (UiAction<U>) constructor.newInstance(
                        new Object[constructor.getParameterTypes().length]);
                constructor.setAccessible(accessible);
                actions.add(action);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return actions;
    }

    /**
     * Delete actions from persistent storage.
     */
    @Override
    public void delete() {
        File file = mApplication.getFilesDir();
        String path = file.getParent() + "/shared_prefs/" + mFileName + ".xml";
        File sharedPrefs = new File(path);
        sharedPrefs.delete();
    }
}
